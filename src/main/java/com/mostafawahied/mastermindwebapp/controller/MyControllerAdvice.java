package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.config.CustomOAuth2User;
import com.mostafawahied.mastermindwebapp.exception.GameException;
import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class MyControllerAdvice {
    private final UserService userService;

    @Autowired
    public MyControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(GameException.class)
    public String handleGameException(GameException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "errorPage";
    }

    // adding the user's name to all pages
    @ModelAttribute
    public void addNameAttribute(Model model) {
        User user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String name = "";
            String email = "";
            String pictureUrl;
            if (principal instanceof CustomOAuth2User customOAuth2User) {
                // handle Google login
                user = userService.findUserByEmail(customOAuth2User.getEmail());
                user.setProfileImageSrc(customOAuth2User.getAttributes().get("picture").toString());
                userService.updateUser(user);
                name = customOAuth2User.getName();
                email = customOAuth2User.getEmail();
                pictureUrl = customOAuth2User.getAttributes().get("picture").toString();
                model.addAttribute("latestBadge", user.getLatestAchievement());
                model.addAttribute("score", user.getScore());
                model.addAttribute("consecutiveWins", user.getConsecutiveWins());
                model.addAttribute("loggedIn", true);
                model.addAttribute("pictureUrl", pictureUrl);
            } else if (principal instanceof UserDetails) {
                // handle local login
                user = userService.findUserByEmail(((UserDetails) principal).getUsername());
                name = user.getUsername();
                email = user.getEmail();
                model.addAttribute("latestBadge", user.getLatestAchievement());
                model.addAttribute("score", user.getScore());
                model.addAttribute("consecutiveWins", user.getConsecutiveWins());
                model.addAttribute("loggedIn", true);
            }
            model.addAttribute("name", name);
            model.addAttribute("email", email);
        }
    }

}
