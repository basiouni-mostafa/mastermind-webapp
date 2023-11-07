package com.mostafawahied.mastermindwebapp.model;

import java.util.concurrent.TimeUnit;

public enum GameDifficulty {

    EASY(4, 10, TimeUnit.DAYS.toMillis(100), 100, 25),

    MEDIUM(5, 20, TimeUnit.MINUTES.toMillis(20), 200, 40),

    HARD(6, 40, TimeUnit.MINUTES.toMillis(30), 400, 60);

    public final int guessLength;
    public final int numOfGuesses;
    public final long gameDuration;
    public final int winPoints;
    public final int losePoints;


    GameDifficulty(int guessLength, int numOfGuesses, long gameDuration, int winPoints, int losePoints) { // number of milliseconds in a game
        this.guessLength = guessLength;
        this.numOfGuesses = numOfGuesses;
        this.gameDuration = gameDuration;
        this.winPoints = winPoints;
        this.losePoints = losePoints;
    }
}
