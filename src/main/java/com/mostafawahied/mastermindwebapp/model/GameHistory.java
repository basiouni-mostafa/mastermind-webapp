package com.mostafawahied.mastermindwebapp.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameHistory {
    private List<String> userGuessList;
    private int correctNumbers;
    private int correctLocations;
    private long remainingMinutes;
}
