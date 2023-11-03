package com.mostafawahied.mastermindwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameResult {
    private int correctNumbers;
    private int correctLocations;
}
