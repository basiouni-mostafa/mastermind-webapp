package com.mostafawahied.mastermindwebapp.manager;

import com.mostafawahied.mastermindwebapp.model.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class GameManager {

    public GameManager() {
        currentGame = null;
        tracker = new ScoreTracker(0, 0);
    }

    private Game currentGame;
    private ScoreTracker tracker;

    private List<String> generateRandomGuess(int numberOfGuesses, GameType type) {
        List<String> responseRandomList = new ArrayList<>();
        if (type.equals(GameType.NUMBERS)) {
            RestTemplate restTemplate = new RestTemplate();
            String responseString =
                    restTemplate.getForObject(
                            String.format("https://www.random.org/integers/?num=%s&min=0&max=7&col=1&base=10&format=plain&rnd=new", numberOfGuesses),
                            String.class);
            // Split the response string on the newline characters
            assert responseString != null;
            responseRandomList = List.of(responseString.split("\n"));

        } else if (type.equals(GameType.COLORS)) {
            final List<String> COLOR_NAMES = List.of("red", "yellow", "green", "brown", "blue", "purple", "orange", "black");
            Random random = new Random();
            for (int i = 0; i < numberOfGuesses; i++) {
                int index = random.nextInt(COLOR_NAMES.size());
                String colorName = COLOR_NAMES.get(index);
                responseRandomList.add(colorName);
            }
        }
        return responseRandomList;
    }

    public Game createGame(GameDifficulty difficulty, GameType type) {
        List<String> randomString = generateRandomGuess(difficulty.guessLength, type);
        // Creating a default "easy" game
        currentGame = new Game(randomString, difficulty.numOfGuesses, type, difficulty, difficulty.gameDuration);
        return currentGame;
    }


    public String validateGuess(List<String> userGuessList) {
        // guess only if state is valid
//        if (!currentGame.getGameState().equals(GameState.IN_PROGRESS)) {
//            return "Game is already finished";
//        }
        // if game type is numbers
        if (currentGame.getGameType().equals(GameType.NUMBERS)) {
            for (String guess : userGuessList) {
                int intGuess;
                try {
                    intGuess = Integer.parseInt(guess);
                } catch (NumberFormatException e) {
                    return String.format("Guess %s is not a number", guess);
                }
                if (intGuess > 7 || intGuess < 0) {
                    return String.format("Guess %s is out of bounds", guess);
                }
            }
            return null;
        } else if (currentGame.getGameType().equals(GameType.COLORS)) {
            for (String guess : userGuessList) {
                try {
                    if (!guess.matches("[a-zA-Z]+")) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
//                    return String.format("Guess %s must contain only letters", String.join("",userGuessList));
                    return String.format("Guess %s must only be a letter", guess);
                }
            }
            return null;
        }
        return null;
    }

    public Game handleGuess(List<String> userGuessList) {

        List<String> randomGuessList = currentGame.getCorrectResult();

        int correctNumbers = 0;
        int correctLocations = 0;
        for (int i = 0; i < userGuessList.size(); i++) {
            if (userGuessList.get(i).equals(randomGuessList.get(i))) {
                correctLocations++;
            } else {
                for (String s : randomGuessList) {
                    if (s.equals(userGuessList.get(i))) {
                        correctNumbers++;
                        break;
                    }
                }
            }
        }
        System.out.println(String.join(",", randomGuessList));
        // Update the state, history, .... etc
        currentGame.setGameRemainingAttempts(currentGame.getGameRemainingAttempts() - 1);

        long remainingSeconds = (currentGame.getGameEndTime() - System.currentTimeMillis()) / 1000;

        currentGame.getGameHistory().add(
                String.format("Guess: %s. Feedback: %s correct number(s) and %s correct locations and %s seconds remaining\n",
                        String.join(" ", userGuessList),
                        correctNumbers,
                        correctLocations,
                        remainingSeconds > 600 ? "unlimited" : remainingSeconds));

        if (correctLocations == currentGame.getCorrectResultLength()) {
            currentGame.setGameState(GameState.WON);
            tracker.setNumWins(tracker.getNumWins() + 1);
        } else if (currentGame.getGameRemainingAttempts() == 0 ||
                System.currentTimeMillis() > currentGame.getGameEndTime()) {
            currentGame.setGameState(GameState.LOST);
            tracker.setNumLosses(tracker.getNumLosses() + 1);
        }
        return currentGame;
    }

    // to render page at hint method
    public Game getGame() {
        return currentGame;
    }

    public ScoreTracker getTracker() {
        return tracker;
    }

    public String getHint(List<String> correctResult, List<String> userGuessList) {
        correctResult = currentGame.getCorrectResult();
        List<String> correctResultList = new ArrayList<>();
        for (String element : correctResult) {
            if (!userGuessList.contains(element)) {
                correctResultList.add(element);
            }
        }
        // pick a random index to get a random number from the list
        Random random = new Random();
        int index = 0;
        String hint = "";
        try {
            index = random.nextInt(correctResultList.size());
            hint = correctResultList.get(index);
        } catch (IllegalArgumentException e) {
            index = random.nextInt(userGuessList.size());
            hint = userGuessList.get(index);
        }

        return hint;
    }

}
