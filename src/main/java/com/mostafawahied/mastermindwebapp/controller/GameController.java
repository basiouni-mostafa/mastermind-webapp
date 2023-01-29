package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.manager.GameManager;
import com.mostafawahied.mastermindwebapp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class GameController {
    @Autowired
    private GameManager gameManager;

    @GetMapping("/")
    public String homePage(Model model) {
        Game game = null;
        try {
            game = gameManager.createGame(GameDifficulty.EASY, GameType.NUMBERS); // WILL take parameters. Handle exception by try catch, add attribute for error message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create game. Try again.");
        }
        assert game != null;
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(tracker, model);
        addAttributes(game, model);

        return "index";
    }

    @PostMapping("/guess")
    public String guess(@RequestParam("userGuesses") List<String> guesses, Model model) {
        String validationProblem = gameManager.validateGuess(guesses);
        if (validationProblem != null) {
            model.addAttribute("validationFailure", validationProblem);
            return "index";
        }
        Game game = gameManager.handleGuess(guesses);
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(game, model);
        addAttributes(tracker, model);
        // adding hint to the template by using the getHint method
        String hintString = gameManager.getHint(guesses);
        model.addAttribute("hint", hintString);

        return "index";
    }

    @GetMapping("/new_game")
    public String newGame(@RequestParam("difficulty") String userDifficulty,
                          @RequestParam("type") String userGameType,
                          Model model) {

        Game game;
        try {
            GameDifficulty difficulty = GameDifficulty.valueOf(userDifficulty);
            GameType gameType = GameType.valueOf(userGameType);
            game = gameManager.createGame(difficulty, gameType); // WILL take parameters. Handle exception by try catch, add attribute for error message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create game. Try again later.");
            return "index";
        }
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(tracker, model);
        addAttributes(game, model);

        return "index";
    }

    public void addAttributes(ScoreTracker tracker, Model model) {
        model.addAttribute("numOfWins", tracker.getNumWins());
        model.addAttribute("numOfLosses", tracker.getNumLosses());
    }

    private void addAttributes(Game game, Model model) {
        model.addAttribute("remainingAttempts", game.getGameRemainingAttempts());
        model.addAttribute("gameState", game.getGameState().toString());
        model.addAttribute("gameHistory", game.getGameHistory());
        model.addAttribute("originalGuessCount", game.getOriginalGuessCount());
        model.addAttribute("correctNumber", String.join(", ", game.getCorrectResult()));
        model.addAttribute("gameType", game.getGameType().toString());
        model.addAttribute("gameDifficulty", game.getGameDifficulty().toString());
        System.out.println(game.getCorrectResult());
    }
}
