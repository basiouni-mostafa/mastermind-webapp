package com.mostafawahied.mastermindwebapp.manager;

import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class GameManager {
    private UserService userService;
    @Autowired
    public GameManager(UserService userService) {
        this.userService = userService;
        currentGame = null;
    }

    GameManager(Game game) {
        currentGame = game;
    }

    private Game currentGame;
    private User currentUser;

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

    public Game createGame(GameDifficulty difficulty, GameType type, User user) {
        List<String> randomString = generateRandomGuess(difficulty.guessLength, type);
        // Creating a default "easy" game
        currentGame = new Game(randomString, type, difficulty);
        this.currentUser = user;
        return currentGame;
    }

    public String validateGuess(List<String> userGuessList) {

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

    public Game processUserGuess(List<String> userGuessList, Model model) {

        List<String> randomGuessList = currentGame.getCorrectResult();

        int correctNumbers = 0;
        int correctLocations = 0;
        // Count number of values guesses that are in the result
        final Set<String> resultDigits = new HashSet<>(randomGuessList);
        final Set<String> guessDigits = new HashSet<>(userGuessList);
        guessDigits.retainAll(resultDigits);
        correctNumbers = guessDigits.size();
        // Check correct locations
        for (int index = 0; index < userGuessList.size(); index++) {
            if (userGuessList.get(index).equals(randomGuessList.get(index))) {
                correctLocations++;
            }
        }
        // Update the state, history, .... etc
        currentGame.setGameRemainingAttempts(currentGame.getGameRemainingAttempts() - 1);

        long remainingSeconds = (currentGame.getGameEndTime() - System.currentTimeMillis()) / 1000;
        long remainingMinutes = remainingSeconds / 60;

        currentGame.getGameHistory().add(new GameHistory(userGuessList,
                correctNumbers,
                correctLocations,
                remainingMinutes));

        if (correctLocations == currentGame.getCorrectResultLength()) {
            currentGame.setGameState(GameState.WON);
            if (currentUser != null) {
                String bonusWon = currentUser.recordWin(currentGame).orElse("");
                model.addAttribute("bonusWon", bonusWon);
                userService.save(currentUser);
            }
        } else if (currentGame.getGameRemainingAttempts() == 0 ||
                System.currentTimeMillis() > currentGame.getGameEndTime()) {
            currentGame.setGameState(GameState.LOST);
            if (currentUser != null) {
                String bonusWon = currentUser.recordLoss(currentGame).orElse("");
                model.addAttribute("bonusWon", bonusWon);
                userService.save(currentUser);
            }
        }
        return currentGame;
    }

    // to render page at hint method
    public Game getGame() {
        return currentGame;
    }

    public String getHint(List<String> userGuessList) {
        List<String> correctResult = currentGame.getCorrectResult();
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
