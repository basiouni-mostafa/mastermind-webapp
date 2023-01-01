package com.mostafawahied.mastermindwebapp.model;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class Game {
    private String gameRandomNumberString;
    private int gameRemainingAttempts;
    private String gameHistory;
    private String gameState;

    public Game() {
        this.gameRandomNumberString = generateRandomNumber();
        this.gameRemainingAttempts = 10;
        this.gameHistory = "";
        this.gameState = "in progress";
    }

    public String generateRandomNumber() {
        RestTemplate restTemplate = new RestTemplate();
        String responseString =
                restTemplate.getForObject(
                        "https://www.random.org/integers/?num=4&min=0&max=7&col=1&base=10&format=plain&rnd=new",
                        String.class);
//        Split the response string on the newline characters
        String[] responseNumbersArray = responseString.split("\n");
        List<Integer> randomNumberList = new ArrayList<>();
//        Parse each individual integer and add it to the randomList
        for (String responseNumber : responseNumbersArray) {
            randomNumberList.add(Integer.parseInt(responseNumber));
        }

        return String.join("", responseNumbersArray);
    }
}
