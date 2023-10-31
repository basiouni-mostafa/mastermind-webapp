package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.config.CustomOAuth2User;
import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.Map;

@ControllerAdvice
public class MyControllerAdvice {
    @Autowired
    private UserRepository userRepository;
    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
//        // adding the user's name
//        String name = userService.getLoggedInUserName(authentication);
//        model.addAttribute("name", name);
        // adding the user's profile picture
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
            // Get the user's profile picture URL from the OAuth2 authentication
            String pictureUrl = oauth2Authentication.getPrincipal().getAttributes().get("picture").toString();
            model.addAttribute("pictureUrl", pictureUrl);

        }
    }

    // adding the user's name to all pages
    @ModelAttribute
    public void addNameAttribute(Model model, HttpSession session) {
        User user;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            String name = "";
            String email = "";
            if (principal instanceof CustomOAuth2User customOAuth2User) {
                // handle Google login
                user = userRepository.findUserByEmail(customOAuth2User.getEmail());
                user.setProfileImageSrc(customOAuth2User.getAttributes().get("picture").toString());
                userRepository.save(user);
                name = customOAuth2User.getName();
                email = customOAuth2User.getEmail();
                model.addAttribute("score", user.getScore());
                model.addAttribute("consecutiveWins", user.getConsecutiveWins());
                model.addAttribute("loggedIn", true);
            } else if (principal instanceof UserDetails) {
                // handle local login
//                User user = userRepository.findUserByEmail(((UserDetails) principal).getUsername());
                user = userRepository.findUserByEmail(((UserDetails) principal).getUsername());
                name = user.getUsername();
                email = user.getEmail();
                model.addAttribute("score", user.getScore());
                model.addAttribute("consecutiveWins", user.getConsecutiveWins());
                model.addAttribute("loggedIn", true);
            }
            model.addAttribute("name", name);
            model.addAttribute("email", email);
        }
    }

}
