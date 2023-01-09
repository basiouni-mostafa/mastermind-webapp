//package com.mostafawahied.mastermindwebapp.controller;
//
//import com.mostafawahied.mastermindwebapp.model.GameOld;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//public class GameControllerOld {
//    @Autowired
//    private GameOld gameOLD;
//
//    @GetMapping("/")
//    public String homePage(Model model) {
//        model.addAttribute("game", gameOLD);
//        return "index_oldest";
//    }
//
//    @PostMapping("/guess")
//    public String guess(@RequestParam(name = "user_guess") String userGuessString, Model model) {
//        // Start of GameController
////        Split the user guess string
//        String[] userGuessArray = userGuessString.split("");
////        Parse each individual integer and add it to the user number list
//        List<Integer> userNumberList = new ArrayList<>();
//        for (String number : userGuessArray) {
//            userNumberList.add(Integer.parseInt(number));
//        }
//
////        Split the random guess string
//        String[] randomNumberArray = gameOLD.getGameRandomNumberString().split("");
////        Parse each individual integer and add it to the random number list
//        List<Integer> randomNumberList = new ArrayList<>();
//        for (String number : randomNumberArray) {
//            randomNumberList.add(Integer.parseInt(number));
//        }
//
//        // Start of Game Manager
//        int correctNumbers = 0;
//        int correctLocations = 0;
//        for (int i = 0; i < userNumberList.size(); i++) {
//            if (userNumberList.get(i).equals(randomNumberList.get(i))) {
//                correctLocations++;
//            } else {
//                for (int j = 0; j < randomNumberList.size(); j++) {
//                    if (randomNumberList.get(j).equals(userNumberList.get(i))) {
//                        correctNumbers++;
//                        break;
//                    }
//                }
//            }
//        }
//        System.out.println(gameOLD.getGameRandomNumberString());
//
//        //
//        // Decrement the remaining attempts
//        gameOLD.setGameRemainingAttempts(gameOLD.getGameRemainingAttempts() - 1);
//        // Update the game history with the player's guess and the feedback
//        gameOLD.setGameHistory(gameOLD.getGameHistory() + "Guess: " + userGuessString + " Feedback: " + correctNumbers + " correct number(s) and " + correctLocations + " correct location(s)\n");
//        // Add the game object to the model to access its fields in the Thymeleaf template
//        model.addAttribute("game", gameOLD);
//        // If the player has used all their attempts, end the game and display the final result
//        if (gameOLD.getGameRemainingAttempts() == 0) {
//
//                gameOLD.setGameState("lost");
//
//
//        }
//        if (correctLocations == 4) {
//            gameOLD.setGameState("won");
//        }
//
//        return "redirect:/";
//    }
//
//    @GetMapping("/new_game")
//    public String newGame(Model model) {
//        gameOLD = new GameOld();
//        model.addAttribute("game", gameOLD);
//        return "index_oldest";
//    }
//
//}
