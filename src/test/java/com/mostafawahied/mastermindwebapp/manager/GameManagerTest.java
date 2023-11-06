package com.mostafawahied.mastermindwebapp.manager;

import com.google.common.collect.ImmutableList;
import com.mostafawahied.mastermindwebapp.manager.guessValidator.ColorGuessValidator;
import com.mostafawahied.mastermindwebapp.manager.guessValidator.GuessValidator;
import com.mostafawahied.mastermindwebapp.manager.guessValidator.NumberGuessValidator;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGenerator;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGeneratorRetriever;
import com.mostafawahied.mastermindwebapp.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameManagerTest {
    private final GameDifficulty DIFFICULTY_EASY = GameDifficulty.EASY;
    private final GameType NUMBER_TYPE = GameType.NUMBERS;
    private final GameType COLOR_TYPE = GameType.COLORS;
    private final List<String> SOLUTION_NUMBER_LIST = ImmutableList.of("1", "2", "3", "4");
    private final List<String> SOLUTION_COLOR_LIST = ImmutableList.of("RED", "BLUE", "GREEN", "YELLOW");
    private final List<String> CORRECT_GUESS_NUMBER_LIST = ImmutableList.of("1", "2", "3", "4");
    private final List<String> CORRECT_GUESS_COLOR_LIST = ImmutableList.of("RED", "BLUE", "GREEN", "YELLOW");
    private final List<String> INCORRECT_GUESS_NUMBER_LIST = ImmutableList.of("8", "-1", "3", "a");
    private final List<String> INCORRECT_GUESS_COLOR_LIST = ImmutableList.of("RED", "BLUE", "GREEN", "1");

    @Mock
    private SolutionGeneratorRetriever generatorRetriever;
    @Mock
    private SolutionGenerator solutionGenerator;

    @InjectMocks
    private GameManager gameManager;

    @Test
    void createGame_createsCorrectGameForNumbers() {
        // Arrange
        when(generatorRetriever.retrieveSolutionGenerator(NUMBER_TYPE)).thenReturn(solutionGenerator);
        when(solutionGenerator.generateSolution(DIFFICULTY_EASY.guessLength)).thenReturn(SOLUTION_NUMBER_LIST);
        // Act
        Game game = gameManager.createGame(DIFFICULTY_EASY, NUMBER_TYPE);
        // Assert
        assertNotNull(game);
        assertEquals(DIFFICULTY_EASY, game.getGameDifficulty());
        assertEquals(NUMBER_TYPE, game.getGameType());
        assertEquals(DIFFICULTY_EASY.guessLength, game.getSolution().size());
        assertEquals(game.getSolution().get(0), SOLUTION_NUMBER_LIST.get(0));
    }

    @Test
    void createGame_createsCorrectGameForColors() {
        // Arrange
        when(generatorRetriever.retrieveSolutionGenerator(COLOR_TYPE)).thenReturn(solutionGenerator);
        when(solutionGenerator.generateSolution(DIFFICULTY_EASY.guessLength)).thenReturn(SOLUTION_COLOR_LIST);
        // Act
        Game game = gameManager.createGame(DIFFICULTY_EASY, COLOR_TYPE);
        // Assert
        assertNotNull(game);
        assertEquals(DIFFICULTY_EASY, game.getGameDifficulty());
        assertEquals(COLOR_TYPE, game.getGameType());
        assertEquals(DIFFICULTY_EASY.guessLength, game.getSolution().size());
        assertEquals(game.getSolution().get(0), SOLUTION_COLOR_LIST.get(0));
    }

    @Test
    void validateGuess_addsErrorForOutOfBoundsNumbers() {
        // Arrange
        Game mockGame = mock(Game.class);
        NumberGuessValidator numberGuessValidator = new NumberGuessValidator(); // Use the actual validator
        List<String> validationErrors = new ArrayList<>();

        // Act
        numberGuessValidator.validateGuess(mockGame, INCORRECT_GUESS_NUMBER_LIST, validationErrors);

        // Assert
        assertEquals(3, validationErrors.size());
        assertTrue(validationErrors.contains("Guess 8 is out of bounds"));
        assertTrue(validationErrors.contains("Guess -1 is out of bounds"));
        assertTrue(validationErrors.contains("Guess a is not a number"));
    }

    @Test
    void validateGuess_addsErrorForInvalidColors() {
        // Arrange
        Game mockGame = mock(Game.class);
        GuessValidator colorGuessValidator = new ColorGuessValidator();
        List<String> validationErrors = new ArrayList<>();

        // Act
        colorGuessValidator.validateGuess(mockGame, INCORRECT_GUESS_COLOR_LIST, validationErrors);

        // Assert
        assertEquals(1, validationErrors.size());
        assertTrue(validationErrors.contains("Guess 1 is invalid"));
    }

    @Test
    void validateGuess_doesNotAddErrorForCorrectNumbers() {
        // Arrange
        Game mockGame = mock(Game.class);
        NumberGuessValidator numberGuessValidator = new NumberGuessValidator();
        List<String> validationErrors = new ArrayList<>();

        // Act
        numberGuessValidator.validateGuess(mockGame, CORRECT_GUESS_NUMBER_LIST, validationErrors);

        // Assert
        assertTrue(validationErrors.isEmpty(), "No validation errors should be present for correct number guesses");
    }

    @Test
    void validateGuess_doesNotAddErrorForCorrectColors() {
        // Arrange
        Game mockGame = mock(Game.class);
        ColorGuessValidator colorGuessValidator = new ColorGuessValidator();
        List<String> validationErrors = new ArrayList<>();

        // Act
        colorGuessValidator.validateGuess(mockGame, CORRECT_GUESS_COLOR_LIST, validationErrors);

        // Assert
        assertTrue(validationErrors.isEmpty(), "No validation errors should be present for correct color guesses");
    }

}

