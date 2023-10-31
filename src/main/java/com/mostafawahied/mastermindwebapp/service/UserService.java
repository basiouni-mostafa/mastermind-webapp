package com.mostafawahied.mastermindwebapp.service;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.exception.UserNotFoundException;
import com.mostafawahied.mastermindwebapp.model.AuthenticationProvider;
import com.mostafawahied.mastermindwebapp.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);

    User save(User user);

    User findUserById(long id);


    User findUserByEmail(String email);

    User findUserByUsername(String username);

    void createNewUserAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider);

    void updateUserAfterOAuthLoginSuccess(User userEntity, String name, AuthenticationProvider authenticationProvider);

    void updateResetPasswordToken(String token, String email) throws UserNotFoundException;

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);
}