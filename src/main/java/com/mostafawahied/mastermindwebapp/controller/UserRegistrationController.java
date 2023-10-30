package com.mostafawahied.mastermindwebapp.controller;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.exception.DuplicateEmailException;
import com.mostafawahied.mastermindwebapp.exception.DuplicateUsernameException;
import com.mostafawahied.mastermindwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {
        try {
            userService.save(registrationDto);
            // Authenticate the user
            authenticateUser(registrationDto.getEmail(), registrationDto.getPassword());
            return "redirect:/";
        } catch (DuplicateEmailException e) {
            // Redirect with emailError parameter if there is a duplicate email
            return "redirect:/login?emailError";
        } catch (DuplicateUsernameException e) {
            // Redirect with usernameError parameter if there is a duplicate username
            return "redirect:/login?usernameError";
        } catch (Exception allExceptions) {
            System.out.println(allExceptions.getMessage());
            return "redirect:/login?RegisterError";
        }
    }

    // helper method for the auto login
    private void authenticateUser(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}

