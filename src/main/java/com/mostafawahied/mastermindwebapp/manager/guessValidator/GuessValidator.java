package com.mostafawahied.mastermindwebapp.manager.guessValidator;

import com.mostafawahied.mastermindwebapp.model.Game;

import java.util.List;

public interface GuessValidator {
     void validateGuess(Game game, List<String> userGuessList, List<String> validationErrors);
}
