package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.manager.GameManager;
import com.mostafawahied.mastermindwebapp.model.*;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.*;

@Controller
@SessionAttributes("sessionGame")
public class GameController {
    private final GameManager gameManager;
    private final UserService userService;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public GameController(GameManager gameManager, UserService userService) {
        this.gameManager = gameManager;
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        User currentUser = userService.getCurrentUser();
        Game game = gameManager.createGame(GameDifficulty.EASY, GameType.NUMBERS);
        addAttributes(game, model, currentUser);
        return "index";
    }

    @PostMapping("/guess")
    public String guess(@RequestParam("userGuesses") List<String> guesses,
                        Model model,
                        SessionStatus status) {
        Game game = (Game) model.getAttribute("sessionGame");
        gameManager.validateGuess(game, guesses);
        User currentUser = userService.getCurrentUser();
        game = gameManager.processUserGuess(game, guesses, currentUser);
        addAttributes(game, model, currentUser);
        String hintString = gameManager.getHint(game, guesses);
        model.addAttribute("hint", hintString);
        if (!game.getGameState().equals(GameState.IN_PROGRESS)) {
            status.setComplete();
        }
        return "index";
    }

    @GetMapping("/new_game")
    public String newGame(@RequestParam("difficulty") String userDifficulty,
                          @RequestParam("type") String userGameType,
                          Model model) {
        User currentUser = userService.getCurrentUser();
        GameDifficulty difficulty = GameDifficulty.valueOf(userDifficulty);
        GameType gameType = GameType.valueOf(userGameType);
        Game game = gameManager.createGame(difficulty, gameType);
        addAttributes(game, model, currentUser);
        return "index";
    }

    private void addAttributes(Game game, Model model, User currentUser) {
        model.addAttribute("sessionGame", game); // this is converted to a session attribute. It's saved in the session store.
        model.addAttribute("remainingAttempts", game.getGameRemainingAttempts());
        model.addAttribute("gameState", game.getGameState().toString());
        model.addAttribute("gameHistory", game.getGameHistory());
        model.addAttribute("originalGuessCount", game.getOriginalGuessCount());
        if (game.getGameState() == GameState.WON || game.getGameState() == GameState.LOST) {
            model.addAttribute("solution", String.join(", ", game.getSolution()));
        }
        model.addAttribute("gameType", game.getGameType().toString());
        model.addAttribute("gameDifficulty", game.getGameDifficulty().toString());
        model.addAttribute("hint", "Hint available after first guess");
        if (currentUser != null) {
            model.addAttribute("score", currentUser.getScore());
            model.addAttribute("consecutiveWins", currentUser.getConsecutiveWins());
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        model.addAttribute("notification", game.getNotification());
        logger.info("Current game solution: " + game.getSolution());
    }
}
