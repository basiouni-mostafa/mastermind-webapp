package com.mostafawahied.mastermindwebapp.manager.solutiongenerator;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mostafawahied.mastermindwebapp.manager.solutiongenerator.ColorRandomSolutionGenerator.COLOR_NAMES;
import static org.junit.jupiter.api.Assertions.*;

class ColorRandomSolutionGeneratorTest {

    ColorRandomSolutionGenerator generator;

    Set<String> VALID_COLORS = new HashSet<>(COLOR_NAMES);
    @Test
    void generateSolution_withSmallInput_thenReturnValidResults() {
        List<String> solutions = generator.generateSolution(2);
        assertEquals(solutions.size(), 2);
        for (String color : solutions) {
            assertTrue(VALID_COLORS.contains(color));
        }
    }

    @Test
    void generateSolution_withLargeInput_thenReturnValidResults() {
        List<String> solutions = generator.generateSolution(500);
        assertEquals(solutions.size(), 500);
        for (String color : solutions) {
            assertTrue(VALID_COLORS.contains(color));
        }
    }

}