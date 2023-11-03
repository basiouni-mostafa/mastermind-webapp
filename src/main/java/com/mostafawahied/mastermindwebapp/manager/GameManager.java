package com.mostafawahied.mastermindwebapp.manager;

import com.mostafawahied.mastermindwebapp.exception.GameException;
import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class GameManager {
    private Game currentGame;
    private User currentUser;
    private UserService userService;

    @Autowired
    public GameManager(UserService userService) {
        this.userService = userService;
        currentGame = null;
    }

    GameManager(Game game) {
        currentGame = game;
    }

    private static final int MIN_NUMBER = 0;
    private static final int MAX_NUMBER = 7;
    private static final String RANDOM_API_URL = "https://www.random.org/integers/?num=%s&min=" + MIN_NUMBER + "&max=" + MAX_NUMBER + "&col=1&base=10&format=plain&rnd=new";
    private static final String COLOR_REGEX = "^[a-zA-Z]+$";

    private List<String> generateRandomGameElements(int numberOfGuesses, GameType type) {
        List<String> randomGuessList = new ArrayList<>();
        if (type == (GameType.NUMBERS)) {
            randomGuessList = generateRandomNumberList(numberOfGuesses);
        } else if (type == (GameType.COLORS)) {
            randomGuessList = generateRandomColorList(numberOfGuesses);
        }
        return randomGuessList;
    }

    private List<String> generateRandomNumberList(int numberOfGuesses) {
        RestTemplate restTemplate = new RestTemplate();
        String numberGuessString;
        try {
            numberGuessString = restTemplate.getForObject(String.format(RANDOM_API_URL, numberOfGuesses), String.class);
            if (numberGuessString == null) {
                throw new GameException("Error generating random numbers");
            }
        } catch (RestClientException e) {
            throw new GameException("Error reaching random.org");
        }
        return List.of(numberGuessString.split("\n"));
//        return Stream.of(1, 1, 3, 5).map(String::valueOf).toList();
    }

    private List<String> generateRandomColorList(int numberOfGuesses) {
        List<String> randomColorList = new ArrayList<>();
        final List<String> COLOR_NAMES = List.of("red", "yellow", "green", "brown", "blue", "purple", "orange", "black");
        Random random = new Random();
        for (int i = 0; i < numberOfGuesses; i++) {
            int index = random.nextInt(COLOR_NAMES.size());
            String randomColor = COLOR_NAMES.get(index);
            randomColorList.add(randomColor);
        }
        return randomColorList;
    }

    public Game createGame(GameDifficulty difficulty, GameType type, User user) {
        List<String> randomString = generateRandomGameElements(difficulty.guessLength, type);
        // Creating a default "easy" game
        currentGame = new Game(randomString, type, difficulty);
        this.currentUser = user;
        return currentGame;
    }

    public void validateGuess(List<String> userGuessList) throws GameException {
        List<String> validationErrors = new ArrayList<>();
        switch (currentGame.getGameType()) {
            case NUMBERS:
                validateNumberGuess(userGuessList, validationErrors);
                break;
            case COLORS:
                validateColorGuess(userGuessList, validationErrors);
                break;
            default:
                throw new GameException("Invalid game type");
        }

        if (!validationErrors.isEmpty()) {
            throw new GameException(String.join("\n", validationErrors));
        }
    }


    private void validateNumberGuess(List<String> userGuessList, List<String> validationErrors) {
        for (String guess : userGuessList) {
            try {
                int intGuess = Integer.parseInt(guess);
                if (intGuess > MAX_NUMBER || intGuess < MIN_NUMBER) {
                    validationErrors.add(String.format("Guess %s is out of bounds", guess));
                }
            } catch (NumberFormatException e) {
                validationErrors.add(String.format("Guess %s is not a number", guess));
            }
        }
    }

    private void validateColorGuess(List<String> userGuessList, List<String> validationErrors) {
        for (String guess : userGuessList) {
            if (!guess.matches(COLOR_REGEX)) {
                validationErrors.add(String.format("Guess %s is invalid", guess));
            }
        }
    }

    public Game processUserGuess(List<String> userGuessList, Model model) {
        GameResult gameResult = calculateGameResult(userGuessList);
        updateGameHistory(gameResult, userGuessList);
        updateGameState(gameResult, gameResult.getCorrectLocations());
        return currentGame;
    }

    private void updateGameState(GameResult gameResult, int correctLocations) {
        if (correctLocations == currentGame.getCorrectResultLength()) {
            currentGame.setGameState(GameState.WON);
            if (currentUser != null) {
                currentGame.setBonus(currentUser.recordWin(currentGame).orElse(""));
                userService.save(currentUser);
            }
        } else if (currentGame.getGameRemainingAttempts() == 0 || currentGame.isTimeUp()) {
            currentGame.setGameState(GameState.LOST);
            if (currentUser != null) {
                currentGame.setBonus(currentUser.recordLoss(currentGame).orElse(""));
                userService.save(currentUser);
            }
        }
    }

    //    create a method to update the game history
    private void updateGameHistory(GameResult gameResult, List<String> userGuessList) {
        int correctNumbers = gameResult.getCorrectNumbers();
        int correctLocations = gameResult.getCorrectLocations();

        currentGame.setGameRemainingAttempts(currentGame.getGameRemainingAttempts() - 1);

        long remainingSeconds = (currentGame.getGameEndTime() - System.currentTimeMillis()) / 1000;
        long remainingMinutes = remainingSeconds / 60;

        currentGame.getGameHistory().add(new GameHistory(userGuessList, correctNumbers, correctLocations, remainingMinutes));
    }

    //    create a method to calculate the result of the game that returns the number of correct numbers and correct locations
    private GameResult calculateGameResult(List<String> userGuessList) {
        int correctNumbers = 0;
        int correctLocations = 0;
        List<String> correctResultList = currentGame.getCorrectResult();
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

    // to render page at hint method
    public Game getGame() {
        return currentGame;
    }

    public String getHint(List<String> userGuessList) {
        List<String> correctResult = currentGame.getCorrectResult();
        List<String> hints = new ArrayList<>(correctResult);
        hints.removeAll(userGuessList);
        if (hints.isEmpty()) {
            return "No hints available.";
        }
        Random random = new Random();
        return hints.get(random.nextInt(hints.size()));
    }

}
