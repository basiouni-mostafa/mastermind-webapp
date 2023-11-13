package com.mostafawahied.mastermindwebapp.manager;

import com.mostafawahied.mastermindwebapp.model.Game;
import com.mostafawahied.mastermindwebapp.model.GameHistory;
import com.mostafawahied.mastermindwebapp.model.GameState;
import com.mostafawahied.mastermindwebapp.model.User;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GameSummaryGenerator {
    public String generateGameSummary(Game game, User currentUser) {

        StringBuilder summary = new StringBuilder();
        int points = game.getGameState() == GameState.WON ? game.getGameDifficulty().winPoints : game.getGameDifficulty().losePoints;

        if (currentUser == null) {
            summary.append("👤 Player's Game:\n");
        } else {
            String badge = currentUser.getLatestAchievement();
            // Player's name and game details
            summary.append(String.format("👤 %s's Game:\n", currentUser.getUsername()));
            // Badge, Score and points
            summary.append(String.format("%s 💰 Score: %d | 🎖️ %s\n",
                    !Objects.equals(badge, "") ? "🏆 " + badge + " | " : "", currentUser.getScore(), game.getGameState() == GameState.WON ? "+" + points : "-" + points));
        }
        // Game attempts and results
        switch (game.getGameDifficulty()) {
            case EASY:
                summary.append(generateAttemptsForEasy(game)).append("\n");
                break;
            case MEDIUM, HARD:
                summary.append(generateKeyAttemptsForMediumAndHard(game));
                break;
        }
        // Game outcome
        if (game.getGameState().equals(GameState.WON)) {
            summary.append(String.format("🎉 WON in %d attempt/s!\n", game.getOriginalGuessCount() - game.getGameRemainingAttempts()));
        } else {
            summary.append(String.format("😢 LOST after %d attempt/s!\n", game.getOriginalGuessCount() - game.getGameRemainingAttempts()));
        }

        game.setGameSummary(summary.toString());
        return summary.toString();
    }

    private String generateAttemptsForEasy(Game game) {
        StringBuilder attemptsSummary = new StringBuilder();
        int attemptNumber = 1; // Initialize attempt number
        for (GameHistory history : game.getGameHistory()) {
            attemptsSummary.append(generateAttemptString(history, game, attemptNumber++)); // Increment attempt number
        }
        return attemptsSummary.toString();
    }

    private String generateAttemptString(GameHistory history, Game game, int attemptNumber) {
        StringBuilder attemptString = new StringBuilder();
        List<String> guess = history.getUserGuessList();
        List<String> solution = game.getSolution();

        Map<String, Integer> solutionFrequency = new HashMap<>();
        for (String number : solution) {
            solutionFrequency.put(number, solutionFrequency.getOrDefault(number, 0) + 1);
        }

        boolean[] correctPositions = new boolean[guess.size()];
        boolean[] matchedNumbers = new boolean[guess.size()];

        // First pass: check for correct positions (green squares)
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(solution.get(i))) {
                correctPositions[i] = true;
                matchedNumbers[i] = true; // This position is matched
                int count = solutionFrequency.get(guess.get(i));
                solutionFrequency.put(guess.get(i), count - 1); // Decrement the frequency
            }
        }

        // Second pass: check for correct numbers but wrong positions (yellow squares)
        for (int i = 0; i < guess.size(); i++) {
            if (!correctPositions[i]) {
                if (solutionFrequency.getOrDefault(guess.get(i), 0) > 0) {
                    matchedNumbers[i] = true; // This number is matched
                    int count = solutionFrequency.get(guess.get(i));
                    solutionFrequency.put(guess.get(i), count - 1); // Decrement the frequency
                }
            }
        }

        // Construct the attempt string with green, yellow, and black squares
        for (int i = 0; i < guess.size(); i++) {
            if (correctPositions[i]) {
                attemptString.append("🟩");
            } else if (matchedNumbers[i]) {
                attemptString.append("🟨");
            } else {
                attemptString.append("⬛");
            }
        }

        // Append the pipe character only if it's not the last attempt
        if (attemptNumber < game.getGameHistory().size() || game.getGameState() != GameState.WON) {
            attemptString.append("\n");
        }

//        // Prepend the attempt number as an emoji at the start
//        String attemptNumberEmoji = numberToEmoji(attemptNumber);
//        attemptString.insert(0, attemptNumberEmoji + " ");

        return attemptString.toString();
    }

    private String numberToEmoji(int number) {
        StringBuilder emojiBuilder = new StringBuilder();
        String numberStr = String.valueOf(number);
        for (char digit : numberStr.toCharArray()) {
            emojiBuilder.append(digit).append("\u20E3"); // Combining Enclosing Keycap
        }
        return emojiBuilder.toString();
    }


    // Method for generating attempts summary for medium difficulty
    private String generateKeyAttemptsForMediumAndHard(Game game) {
        StringBuilder attemptsSummary = new StringBuilder();
        int attemptNumber = 1;
        int firstCorrect = -1;
        int bestAttemptNum = -1;
        int maxCorrect = 0;
        int maxCorrectPosition = 0;

        for (GameHistory history : game.getGameHistory()) {
            String attempt = generateAttemptString(history, game, attemptNumber);
            int correctCount = countCorrectPositions(history, game);
            int correctColors = countCorrectGuesses(history, game);

            // Check for the first correct attempt (either position or color)
            if (firstCorrect == -1 && (correctCount > 0 || correctColors > 0)) {
                firstCorrect = attemptNumber;
                attemptsSummary.append("🔑 First Correct: ").append(attempt).append("\n");
            }

            // Define a milestone for significant progress, e.g., more than half correct
            int significantProgressThreshold = game.getSolution().size() / 2;

            // Check for the significant progress
            if (correctCount >= significantProgressThreshold && bestAttemptNum == -1) {
                bestAttemptNum = attemptNumber;
            }

            attemptNumber++;
        }

        // If no significant progress has been made, choose the attempt with the most correct guesses
        if (bestAttemptNum == -1 && game.getGameHistory().size() > 1) {
            // Default to the last attempt before the final attempt
            bestAttemptNum = game.getGameHistory().size() - 1;
        }

// Append best attempt if it's different from the first correct and not the final attempt
        if (bestAttemptNum > 0 && bestAttemptNum != firstCorrect && bestAttemptNum < game.getGameHistory().size()) {
            GameHistory bestAttemptHistory = game.getGameHistory().get(bestAttemptNum - 1);
            String bestAttempt = generateAttemptString(bestAttemptHistory, game, bestAttemptNum);
            attemptsSummary.append("🌟 Significant Progress: ").append(bestAttempt).append("\n");
        }

        // Append final attempt
        GameHistory finalAttemptHistory = game.getGameHistory().get(game.getGameHistory().size() - 1);
        String finalAttempt = generateAttemptString(finalAttemptHistory, game, game.getGameHistory().size());
        attemptsSummary.append("🏁 Final Attempt: ").append(finalAttempt).append("\n");

        return attemptsSummary.toString();
    }

    // Helper method to count correct colors in the wrong positions (yellow squares)
    private int countCorrectGuesses(GameHistory history, Game game) {
        List<String> guess = history.getUserGuessList();
        List<String> solution = new ArrayList<>(game.getSolution()); // Copy to manipulate

        // Create a frequency map for the solution to handle duplicates
        Map<String, Integer> solutionFrequency = new HashMap<>();
        for (String color : solution) {
            solutionFrequency.put(color, solutionFrequency.getOrDefault(color, 0) + 1);
        }

        int correctGuesses = 0;
        for (int i = 0; i < guess.size(); i++) {
            if (!guess.get(i).equals(solution.get(i)) && solutionFrequency.getOrDefault(guess.get(i), 0) > 0) {
                correctGuesses++;
                // Decrement the frequency count to prevent double counting
                solutionFrequency.put(guess.get(i), solutionFrequency.get(guess.get(i)) - 1);
            }
        }
        return correctGuesses;
    }

    private int countCorrectPositions(GameHistory history, Game game) {
        List<String> guess = history.getUserGuessList();
        List<String> solution = game.getSolution();
        int correctCount = 0;
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(solution.get(i))) {
                correctCount++;
            }
        }
        return correctCount;
    }

}


/*
the game summary designs we are going to use
Easy Difficulty, Numbers Type, Win
👤 Casey's Game: Mastermind (Easy, Numbers)
🏆 Streak Champion | 💰 Score: 500 | 🎖️ +100 Points
1️⃣ 🟨⬛⬛⬛ | 2️⃣ ⬛⬛🟨⬛ | 3️⃣ 🟨🟨⬛⬛ | 4️⃣ 🟩🟨⬛⬛
5️⃣ 🟩🟩🟨⬛ | 6️⃣ 🟩🟩🟩🟨 | 7️⃣ 🟩🟩🟩🟩
🎉 WON in 7 attempts!

Easy Difficulty, Numbers Type, Loss
👤 Casey's Game: Mastermind (Easy, Numbers)
💰 Score: 400 | 🎖️ -25 Points
1️⃣ 🟨⬛⬛⬛ | 2️⃣ ⬛⬛🟨⬛ | 3️⃣ 🟨🟨⬛⬛ | 4️⃣ ⬛🟨⬛⬛
5️⃣ ⬛🟩🟨⬛ | 6️⃣ ⬛🟩🟩🟨 | 7️⃣ ⬛🟩🟩⬛ | 8️⃣ ⬛⬛🟩🟨
9️⃣ ⬛🟩🟩⬛ | 🔟 ⬛🟩⬛🟨
😢 LOST after 10 attempts.

Medium Difficulty, Colors Type, Win
👤 Casey's Game: Mastermind (Medium, Colors)
🏆 Veteran | 💰 Score: 600 | 🎖️ +150 Points
🔑 First Correct: 2️⃣ 🟨⬛⬛⬛⬛
🌟 Best Attempt: 5️⃣ 🟨🟨🟨🟨⬛
🏁 Final Attempt: 8️⃣ 🟩🟩🟩🟩🟩
🎉 WON in 8 attempts!

Hard Difficulty, Numbers Type, Loss
👤 Casey's Game: Mastermind (Hard, Numbers)
💰 Score: 340 | 🎖️ -60 Points
🔑 First Correct: 5️⃣ 🟨⬛⬛⬛⬛⬛
🌟 Best Attempt: 25️⃣ 🟨🟨🟨🟨⬛⬛
🏁 Final Attempt: 40️⃣ 🟨🟨🟨🟨🟨⬛
😢 LOST after 40 attempts.
 */