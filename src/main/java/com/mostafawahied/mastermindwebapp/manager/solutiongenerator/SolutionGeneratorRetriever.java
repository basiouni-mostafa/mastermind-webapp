package com.mostafawahied.mastermindwebapp.manager.solutiongenerator;

import com.mostafawahied.mastermindwebapp.exception.GameException;
import com.mostafawahied.mastermindwebapp.model.GameType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SolutionGeneratorRetriever {
     private ColorRandomSolutionGenerator colorRandomSolutionGenerator;
     private NumbersRandomSolutionGenerator numbersRandomSolutionGenerator;

    public SolutionGenerator retrieveSolutionGenerator(GameType type) {
        if (type == (GameType.NUMBERS)) {
            return numbersRandomSolutionGenerator;
        } else if (type == (GameType.COLORS)) {
            return colorRandomSolutionGenerator;
        }
        throw new GameException("Invalid game type");
    }

}
