package com.mostafawahied.mastermindwebapp.model;

import java.util.concurrent.TimeUnit;

public enum GameDifficulty {

    EASY(4, 10, TimeUnit.DAYS.toMillis(100)),

    MEDIUM(5, 10, TimeUnit.MINUTES.toMillis(10)),

    HARD(5, 5, TimeUnit.MINUTES.toMillis(5));

    public final int guessLength;
    public final int numOfGuesses;
    public final long gameDuration;
    GameDifficulty(int guessLength, int numOfGuesses, long gameDuration) { // number of milliseconds in a game
        this.guessLength = guessLength;
        this.numOfGuesses = numOfGuesses;
        this.gameDuration = gameDuration;
    }
}
