package com.mostafawahied.mastermindwebapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Game {
    private List<String> correctResult;
    private int correctResultLength;
    private int gameRemainingAttempts;
    private int originalGuessCount;
    private List<String> gameHistory;
    private GameType gameType;
    private GameDifficulty gameDifficulty;
    private GameState gameState;
    private long gameStartTime;
    private long gameEndTime;

    public Game(List<String> correctResult, int remainingAttempts, GameType type, GameDifficulty gameDifficulty, long gameDuration) { // 10
        this.correctResult = correctResult;
        this.correctResultLength = correctResult.size();
        this.gameRemainingAttempts = remainingAttempts; // 10 --> 9, 8, 7
        this.originalGuessCount = remainingAttempts; // 10 --> 10, 10
        this.gameHistory = new ArrayList<>();
        this.gameState = GameState.IN_PROGRESS;
        this.gameType = type;
        this.gameDifficulty = gameDifficulty;
        this.gameStartTime = System.currentTimeMillis();
        this.gameEndTime = this.gameStartTime + gameDuration;
    }
}
