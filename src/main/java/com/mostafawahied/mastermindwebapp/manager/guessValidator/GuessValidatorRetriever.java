package com.mostafawahied.mastermindwebapp.manager.guessValidator;

import com.mostafawahied.mastermindwebapp.exception.GameException;
import com.mostafawahied.mastermindwebapp.model.GameType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GuessValidatorRetriever {

    private NumberGuessValidator numberGuessValidator;
    private ColorGuessValidator colorGuessValidator;

    public GuessValidator retrieveGuessValidator(GameType type) {
        if (type == GameType.NUMBERS) {
            return numberGuessValidator;
        } else if (type == GameType.COLORS) {
            return colorGuessValidator;
        }
        throw new GameException("Invalid game type");
    }
}
