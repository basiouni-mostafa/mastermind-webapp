package com.mostafawahied.mastermindwebapp.manager;

import com.google.common.collect.ImmutableList;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGenerator;
import com.mostafawahied.mastermindwebapp.manager.solutiongenerator.SolutionGeneratorRetriever;
import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameManagerTest {
    private final GameDifficulty DIFFICULTY  = GameDifficulty.EASY;
    private final GameType TYPE = GameType.NUMBERS;

    private final List<String> SOLUTIONS_LIST = ImmutableList.of("1", "2", "3", "4");

    @Mock
    private UserService userService;

    @Mock
    private SolutionGeneratorRetriever generatorRetriever;

    @Mock
    private SolutionGenerator solutionGenerator;

    @InjectMocks
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createGame_createsCorrectGameForNumbers() {
        when(generatorRetriever.retrieveSolutionGenerator(TYPE)).thenReturn(solutionGenerator);
        when(solutionGenerator.generateSolution(DIFFICULTY.guessLength)).thenReturn(SOLUTIONS_LIST);

        // Act
        Game game = gameManager.createGame(DIFFICULTY, TYPE);

        // Assert
        assertNotNull(game);
        assertEquals(DIFFICULTY, game.getGameDifficulty());
        assertEquals(TYPE, game.getGameType());
        assertEquals(DIFFICULTY.guessLength, game.getSolution().size());
        // Iterate with a loop
        assertEquals(game.getSolution().get(0), SOLUTIONS_LIST.get(0));
    }

    @Test
    void createGame() {
        Game game = gameManager.createGame(GameDifficulty.EASY, GameType.NUMBERS);
        assertNotNull(game);
        assertEquals(GameDifficulty.EASY, game.getGameDifficulty());
        assertEquals(GameType.NUMBERS, game.getGameType());
        assertEquals(GameState.IN_PROGRESS, game.getGameState());
        assertEquals(4, game.getSolution().size());
        assertEquals(10, game.getGameRemainingAttempts());
    }

    @Test
    void validateGuess() {
        Game game = gameManager.createGame(GameDifficulty.EASY, GameType.NUMBERS);
        List<String> guesses = Arrays.asList("1", "2", "3", "4");
        gameManager.validateGuess(game, guesses);
    }

}
