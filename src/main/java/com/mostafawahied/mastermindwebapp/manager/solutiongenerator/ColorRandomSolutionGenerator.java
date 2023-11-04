package com.mostafawahied.mastermindwebapp.manager.solutiongenerator;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ColorRandomSolutionGenerator implements SolutionGenerator {
    protected static final List<String> COLOR_NAMES = List.of("red", "orange", "yellow", "green", "blue", "purple", "pink", "brown");

    @Override
    public List<String> generateSolution(int numberOfGuesses) {
        List<String> solutionColorList = new ArrayList<>();
        for (int i = 0; i < numberOfGuesses; i++) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            int index = random.nextInt(COLOR_NAMES.size());
            String randomColor = COLOR_NAMES.get(index);
            solutionColorList.add(randomColor);
        }
        return solutionColorList;
    }
}
