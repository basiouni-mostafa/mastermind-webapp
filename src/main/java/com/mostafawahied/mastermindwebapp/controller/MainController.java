package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
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
}
