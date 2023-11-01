package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.config.CustomOAuth2User;
import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private final UserRepository userRepository;

    public MainController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserRegistrationDto());

        return "login";
    }

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        userRepository.findByOrderByScoreDesc();
        model.addAttribute("topUsers", userRepository.findByOrderByScoreDesc());
        return "leaderboard";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        addAttributes(model, authentication);
        return "profile";
    }

    private void addAttributes(Model model, Authentication authentication) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            User user = null;
            if (principal instanceof CustomOAuth2User) {
                user = userRepository.findUserByEmail(((CustomOAuth2User) authentication.getPrincipal()).getEmail());
            } else if (principal instanceof UserDetails) {
                user = userRepository.findUserByEmail(((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
            }
            assert user != null;
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
            model.addAttribute("winPercentage", user.getWinPercentage());
            model.addAttribute("averageScore", user.getAverageScore());
        }
    }
}
