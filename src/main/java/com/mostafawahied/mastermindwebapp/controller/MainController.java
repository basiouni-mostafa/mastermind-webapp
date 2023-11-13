package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.service.LeaderboardService;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final LeaderboardService leaderboardService;
    private final UserService userService;

    @Autowired
    public MainController(LeaderboardService leaderboardService, UserService userService) {
        this.leaderboardService = leaderboardService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserRegistrationDto());

        return "login";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        model.addAttribute("topUsers", leaderboardService.getTopTenUsers());
        return "leaderboard";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        addAttributes(model);
        return "profile";
    }

    private void addAttributes(Model model) {
        User user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("profileImageSrc", user.getProfileImageSrc());
            model.addAttribute("score", user.getScore());
            model.addAttribute("consecutiveWins", user.getConsecutiveWins());
            model.addAttribute("gamesPlayed", user.getGamesPlayed());
            model.addAttribute("gamesWon", user.getGamesWon());
            model.addAttribute("gamesLost", user.getGamesLost());
            model.addAttribute("mostConsecutiveWins", user.getMostConsecutiveWins());
            model.addAttribute("threeConsecutiveWinsCount", user.getThreeConsecutiveWinsCount()); //
            model.addAttribute("fiveConsecutiveWinsCount", user.getFiveConsecutiveWinsCount()); //
            model.addAttribute("tenConsecutiveWinsCount", user.getTenConsecutiveWinsCount()); //
            model.addAttribute("streakChampionAchievement", user.isStreakChampionAchievement());
            model.addAttribute("veteranAchievement", user.isVeteranAchievement());
            model.addAttribute("mastermindAchievement", user.isMastermindAchievement());
            model.addAttribute("legendAchievement", user.isLegendAchievement());
            model.addAttribute("grandmasterAchievement", user.isGrandmasterAchievement());
            model.addAttribute("championAchievement", user.isChampionAchievement());
            model.addAttribute("winnerAchievement", user.isWinnerAchievement());
            model.addAttribute("enthusiastAchievement", user.isEnthusiastAchievement());
            model.addAttribute("rookieAchievement", user.isRookieAchievement());
            model.addAttribute("newcomerAchievement", user.isNewcomerAchievement());
            model.addAttribute("winPercentage", user.getWinPercentage());
            model.addAttribute("averageScore", user.getAverageScore());
        }
    }

}
