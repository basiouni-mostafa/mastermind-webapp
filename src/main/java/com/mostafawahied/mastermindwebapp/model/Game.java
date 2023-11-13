package com.mostafawahied.mastermindwebapp.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Game {
    private List<String> solution;
    private int solutionLength;
    private int gameRemainingAttempts;
    private int originalGuessCount;
    private List<GameHistory> gameHistory;
    private GameType gameType;
    private GameDifficulty gameDifficulty;
    private GameResult gameResult;
    private GameState gameState;
    private long gameStartTime;
    private long gameEndTime;
    private String notification;
    private String gameSummary;

    public Game(List<String> solution, GameType type, GameDifficulty gameDifficulty) { // 10
        this.solution = solution;
        this.solutionLength = solution.size();
        this.gameRemainingAttempts = gameDifficulty.numOfGuesses;
        this.originalGuessCount = gameDifficulty.numOfGuesses;
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
