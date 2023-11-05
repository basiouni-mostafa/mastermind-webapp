package com.mostafawahied.mastermindwebapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    private List<String> solution;
    private int solutionLength;
    private int gameRemainingAttempts;
    private int originalGuessCount;
    private List<GameHistory> gameHistory;
    private GameType gameType;
    private GameDifficulty gameDifficulty;
    private GameState gameState;
    private long gameStartTime;
    private long gameEndTime;
    private String bonus;

    public Game(List<String> solution, GameType type, GameDifficulty gameDifficulty) { // 10
        this.solution = solution;
        this.solutionLength = solution.size();
        this.gameRemainingAttempts = gameDifficulty.numOfGuesses; // 10 --> 9, 8, 7
        this.originalGuessCount = gameDifficulty.numOfGuesses; // 10 --> 10, 10
        this.gameHistory = new ArrayList<>();
        this.gameState = GameState.IN_PROGRESS;
        this.gameType = type;
        this.gameDifficulty = gameDifficulty;
        this.gameStartTime = System.currentTimeMillis();
        this.gameEndTime = this.gameStartTime + gameDifficulty.gameDuration;
    }

    public boolean isTimeUp() {
        return System.currentTimeMillis() > this.gameEndTime;
    }
}
