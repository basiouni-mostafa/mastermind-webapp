package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {
    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new UserRegistrationDto());

        return "login";
    }
}
