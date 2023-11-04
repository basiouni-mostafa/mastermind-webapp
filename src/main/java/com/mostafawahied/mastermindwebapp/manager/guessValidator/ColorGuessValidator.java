package com.mostafawahied.mastermindwebapp.manager.guessValidator;

import com.mostafawahied.mastermindwebapp.model.Game;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColorGuessValidator implements GuessValidator {
    private static final String COLOR_REGEX = "^[a-zA-Z]+$";
    @Override
    public void validateGuess(Game game, List<String> userGuessList, List<String> validationErrors) {
        for (String guess : userGuessList) {
            if (!guess.matches(COLOR_REGEX)) {
                validationErrors.add(String.format("Guess %s is invalid", guess));
            }
        }
    }
}
