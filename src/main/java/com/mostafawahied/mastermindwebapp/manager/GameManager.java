package com.mostafawahied.mastermindwebapp.manager;

import com.mostafawahied.mastermindwebapp.exception.GameException;
import com.mostafawahied.mastermindwebapp.manager.guessValidator.GuessValidator;
import com.mostafawahied.mastermindwebapp.manager.guessValidator.GuessValidatorRetriever;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGenerator;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGeneratorRetriever;
import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class GameManager {
    private final UserService userService;
    private final SolutionGeneratorRetriever solutionGeneratorRetriever;
    private final GuessValidatorRetriever guessValidationRetriever;
    private final GameSummaryGenerator gameSummaryGenerator = new GameSummaryGenerator();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GameManager.class);

    public GameManager(UserService userService, SolutionGeneratorRetriever solutionGeneratorRetriever, GuessValidatorRetriever guessValidationRetriever) {
        this.userService = userService;
        this.solutionGeneratorRetriever = solutionGeneratorRetriever;
        this.guessValidationRetriever = guessValidationRetriever;
    }

    public Game createGame(GameDifficulty difficulty, GameType type) {
        SolutionGenerator solutionGenerator = solutionGeneratorRetriever.retrieveSolutionGenerator(type);
        List<String> solutionString = solutionGenerator.generateSolution(difficulty.guessLength);
        return new Game(solutionString, type, difficulty);
    }

    public void validateGuess(Game game, List<String> userGuessList) throws GameException {
        if (game == null) {
            throw new GameException("The game is currently unavailable. Please try again later.");
        }
        List<String> validationErrors = new ArrayList<>();
        GuessValidator guessValidator = guessValidationRetriever.retrieveGuessValidator(game.getGameType());
        guessValidator.validateGuess(game, userGuessList, validationErrors);
        if (!validationErrors.isEmpty()) {
            throw new GameException("Validation failed: " + String.join(", ", validationErrors));
        }
    }

    public Game processUserGuess(Game game, List<String> userGuessList, User currentUser) {
        GameResult gameResult = calculateGameResult(game, userGuessList);
        updateGameHistory(game, gameResult, userGuessList);
        updateGameState(game, gameResult.getCorrectLocations(), currentUser);
        game.setGameSummary(gameSummaryGenerator.generateGameSummary(game, currentUser));
        return game;
    }

    private void updateGameState(Game game, int correctLocations, User currentUser) {
        if (correctLocations == game.getSolutionLength()) {
            game.setGameState(GameState.WON);
            logger.info("User won the game");
            if (currentUser != null) {
                game.setNotification(currentUser.recordWin(game).orElse(""));
                userService.updateUser(currentUser);
            }
        } else if (game.getGameRemainingAttempts() == 0 || game.isTimeUp()) {
            game.setGameState(GameState.LOST);
            logger.info("User lost the game");
            if (game.isTimeUp()) {
                game.setNotification("Time's up!");
            }
            if (currentUser != null) {
                game.setNotification(currentUser.recordLoss(game).orElse(""));
                userService.updateUser(currentUser);
            }
        }
    }

    private void updateGameHistory(Game game, GameResult gameResult, List<String> userGuessList) {
        int correctNumbers = gameResult.getCorrectNumbers();
        int correctLocations = gameResult.getCorrectLocations();

        game.setGameRemainingAttempts(game.getGameRemainingAttempts() - 1);

        long remainingSeconds = (game.getGameEndTime() - System.currentTimeMillis()) / 1000;
        long remainingMinutes = remainingSeconds / 60;

        game.getGameHistory().add(new GameHistory(userGuessList, correctNumbers, correctLocations, remainingMinutes));
    }

    private GameResult calculateGameResult(Game game, List<String> userGuessList) {
        int correctNumbers = 0;
        int correctLocations = 0;
        List<String> correctResultList = game.getSolution();
        Map<String, Long> correctResultFrequency = new HashMap<>();

        for (String result : correctResultList) {
            correctResultFrequency.put(result, correctResultFrequency.getOrDefault(result, 0L) + 1);
        }

        for (int index = 0; index < userGuessList.size(); index++) {
            String guess = userGuessList.get(index);
            if (correctResultFrequency.containsKey(guess) && correctResultFrequency.get(guess) > 0) {
                correctNumbers++;
                correctResultFrequency.put(guess, correctResultFrequency.get(guess) - 1);
            }
            if (guess.equals(correctResultList.get(index))) {
                correctLocations++;
            }
        }
        return new GameResult(correctNumbers, correctLocations);
    }

    public String getHint(Game game, List<String> userGuessList) {
        List<String> correctResult = game.getSolution();
        List<String> hints = new ArrayList<>(correctResult);
        hints.removeAll(userGuessList);
        if (hints.isEmpty()) {
            return "No hints available. Try a different combination.";
        }
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return hints.get(random.nextInt(hints.size()));
    }

}
