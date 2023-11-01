package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.manager.GameManager;
import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String homePage(Model model) {
        User currentUser = getCurrentUser();
        Game game = null;
        try {
            game = gameManager.createGame(GameDifficulty.EASY, GameType.NUMBERS, currentUser); // WILL take parameters. Handle exception by try catch, add attribute for error message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create game. Try again.");
        }
        assert game != null;
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(tracker, model);
        addAttributes(game, model, currentUser);

        return "index";
    }

    @PostMapping("/guess")
    public String guess(@RequestParam("userGuesses") List<String> guesses, Model model) {
        String validationProblem = gameManager.validateGuess(guesses);
        if (validationProblem != null) {
            model.addAttribute("validationFailure", validationProblem);
            return "index";
        }
        User currentUser = getCurrentUser();
        Game game = gameManager.processUserGuess(guesses, model);
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(game, model, currentUser);
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
        User currentUser = getCurrentUser();
        Game game;
        try {
            GameDifficulty difficulty = GameDifficulty.valueOf(userDifficulty);
            GameType gameType = GameType.valueOf(userGameType);
            game = gameManager.createGame(difficulty, gameType, currentUser); // WILL take parameters. Handle exception by try catch, add attribute for error message
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create game. Try again later.");
            return "index";
        }
        ScoreTracker tracker = gameManager.getTracker();
        addAttributes(tracker, model);
        addAttributes(game, model, currentUser);

        return "index";
    }

    public void addAttributes(ScoreTracker tracker, Model model) {
        model.addAttribute("numOfWins", tracker.getNumWins());
        model.addAttribute("numOfLosses", tracker.getNumLosses());
    }

    private void addAttributes(Game game, Model model, User currentUser) {
        model.addAttribute("remainingAttempts", game.getGameRemainingAttempts());
        model.addAttribute("gameState", game.getGameState().toString());
        model.addAttribute("gameHistory", game.getGameHistory());
        model.addAttribute("originalGuessCount", game.getOriginalGuessCount());
        model.addAttribute("correctNumber", String.join(", ", game.getCorrectResult()));
        model.addAttribute("gameType", game.getGameType().toString());
        model.addAttribute("gameDifficulty", game.getGameDifficulty().toString());
        if (currentUser != null) {
            model.addAttribute("score", currentUser.getScore());
            model.addAttribute("consecutiveWins", currentUser.getConsecutiveWins());
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        System.out.println(game.getCorrectResult());
    }
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser") || authentication == null) {
            return null;
        }
        // get the current user by username if logged in with oauth or by email if logged in with local
        User currentUser;
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
            currentUser = userService.findUserByEmail(authentication.getName());
        } else {
            currentUser = userService.findUserByUsername(authentication.getName());
        }
        return currentUser;
    }
}
