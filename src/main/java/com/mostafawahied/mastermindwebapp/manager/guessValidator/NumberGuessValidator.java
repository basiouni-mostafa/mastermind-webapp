package com.mostafawahied.mastermindwebapp.manager.guessValidator;

import com.mostafawahied.mastermindwebapp.model.Game;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NumberGuessValidator implements GuessValidator {

    @Override
    public void validateGuess(Game game, List<String> userGuessList, List<String> validationErrors) {
        for (String guess : userGuessList) {
            try {
                int intGuess = Integer.parseInt(guess);
                if (intGuess > 7 || intGuess < 0) {
                    validationErrors.add(String.format("Guess %s is out of bounds", guess));
                }
            } catch (NumberFormatException e) {
                validationErrors.add(String.format("Guess %s is not a number", guess));
            }
        }
    }
}
