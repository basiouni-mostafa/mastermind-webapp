package com.mostafawahied.mastermindwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameHistory {
    private List<String> userGuessList;
    private int correctNumbers;
    private int correctLocations;
    private long remainingMinutes;
}
