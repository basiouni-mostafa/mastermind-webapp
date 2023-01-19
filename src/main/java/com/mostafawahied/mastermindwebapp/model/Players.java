package com.mostafawahied.mastermindwebapp.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class Players {
    private List<Player> players;

    public Players() {
        List<Player> players = new ArrayList<>();
    }
}
