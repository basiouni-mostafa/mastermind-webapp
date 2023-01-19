package com.mostafawahied.mastermindwebapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Player {
    private String name;
    private List<Game> games;
    private ScoreTracker score;

    public Player(String name) {
        this.name = name;
        this.games = new ArrayList<>();
        this.score = new ScoreTracker(0, 0);
    }
}
