package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.model.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GameController {
    @Autowired
    private Game game;

    @GetMapping("/")
    public String homePage(Model model) {
//        GameController.random();
        model.addAttribute("game", game);
        return "index";
    }

    @PostMapping("/guess")
    public String guess(@RequestParam(name = "user_guess") String userGuessString, Model model) {
//        Split the user guess string
        String[] userGuessArray = userGuessString.split("");
//        Parse each individual integer and add it to the user number list
        List<Integer> userNumberList = new ArrayList<>();
        for (String number : userGuessArray) {
            userNumberList.add(Integer.parseInt(number));
        }

//        Split the random guess string
        String[] randomNumberArray = game.getGameRandomNumberString().split("");
//        Parse each individual integer and add it to the random number list
        List<Integer> randomNumberList = new ArrayList<>();
        for (String number : randomNumberArray) {
            randomNumberList.add(Integer.parseInt(number));
        }

        int correctNumbers = 0;
        int correctLocations = 0;
        for (int i = 0; i < userNumberList.size(); i++) {
            if (userNumberList.get(i).equals(randomNumberList.get(i))) {
                correctLocations++;
            } else {
                for (int j = 0; j < randomNumberList.size(); j++) {
                    if (randomNumberList.get(j).equals(userNumberList.get(i))) {
                        correctNumbers++;
                        break;
                    }
                }
            }
        }
        System.out.println(game.getGameRandomNumberString());

        // Decrement the remaining attempts
        game.setGameRemainingAttempts(game.getGameRemainingAttempts() - 1);
        // Update the game history with the player's guess and the feedback
        game.setGameHistory(game.getGameHistory() + "Guess: " + userGuessString + " Feedback: " + correctNumbers + " correct number(s) and " + correctLocations + " correct location(s)\n");
        // Add the game object to the model to access its fields in the Thymeleaf template
        model.addAttribute("game", game);
        // If the player has used all their attempts, end the game and display the final result
        if (game.getGameRemainingAttempts() == 0) {
//            if (correctLocations == 4) {
////                model.addAttribute("result", "You win!");
//                game.setGameState("won");
//            } else {
//                model.addAttribute("result", "You lose. The correct number combination was: " + game.getGameRandomNumberString());
                game.setGameState("lost");
//            }

        }
        if (correctLocations == 4) {
//                model.addAttribute("result", "You win!");
            game.setGameState("won");
//            game.setGameRemainingAttempts(game.getGameRemainingAttempts() + 1);

        }

        return "redirect:/";
    }

    @GetMapping("/newgame")
    public String newGame(Model model) {
        game = new Game();
        model.addAttribute("game", game);
        return "index";
    }

}
