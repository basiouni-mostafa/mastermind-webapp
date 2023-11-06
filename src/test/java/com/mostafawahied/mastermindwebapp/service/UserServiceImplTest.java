package com.mostafawahied.mastermindwebapp.service;

import com.mostafawahied.mastermindwebapp.dto.UserRegistrationDto;
import com.mostafawahied.mastermindwebapp.exception.DuplicateEmailException;
import com.mostafawahied.mastermindwebapp.exception.DuplicateUsernameException;
import com.mostafawahied.mastermindwebapp.exception.UserNotFoundException;
import com.mostafawahied.mastermindwebapp.model.AuthenticationProvider;
import com.mostafawahied.mastermindwebapp.model.User;
import com.mostafawahied.mastermindwebapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto userRegistrationDto;
    private User user;

    @BeforeEach
    void setUp() {
        userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setEmail("test@example.com");
        userRegistrationDto.setUsername("testUser");
        userRegistrationDto.setPassword("testPassword");

        user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setUsername(userRegistrationDto.getUsername());
        user.setPassword(userRegistrationDto.getPassword());
        user.setAuthProvider(AuthenticationProvider.LOCAL);
    }

    @Test
    void saveUser_ShouldSaveNewUser_WhenNoDuplicateEmailOrUsername() {
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.saveUser(userRegistrationDto);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_ShouldThrowDuplicateEmailException_WhenDuplicateEmail() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> userService.saveUser(userRegistrationDto));
    }

    @Test
    void saveUser_ShouldThrowDuplicateUsernameException_WhenDuplicateUsername() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
        // Act & Assert
        assertThrows(DuplicateUsernameException.class, () -> userService.saveUser(userRegistrationDto));
    }

    @Test
    void updateUser_ShouldUpdateUser() {
        // Act
        userService.updateUser(user);
        // Assert
        verify(userRepository).save(user);
    }

    @Test
    void findUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));
        // Act
        User foundUser = userService.findUserById(1L);
        // Assert
        assertEquals(user, foundUser);
    }

    @Test
    void findUserById_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUserById(1L));
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(user);
        // Act
        User foundUser = userService.findUserByEmail("test@example.com");
        // Assert
        assertEquals(user, foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void findUserByEmail_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findUserByEmail(anyString())).thenReturn(null);
        // Act
        User foundUser = userService.findUserByEmail("");
        // Assert
        assertNull(foundUser);
    }

    @Test
    void findUserByUsername_ShouldReturnUser_WhenUserExists() {
        // Arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(user);
        // Act
        User foundUser = userService.findUserByUsername("testUser");
        // Assert
        assertEquals(user, foundUser);
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    void findUserByUsername_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);
        // Act
        User foundUser = userService.findUserByUsername("");
        // Assert
        assertNull(foundUser);
    }

    @Test
    void getCurrentUser_ShouldReturnNull_WhenNotAuthenticated() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.setContext(securityContext);
        // Act
        User currentUser = userService.getCurrentUser();
        // Assert
        assertNull(currentUser, "Expected null user when not authenticated");
    }

    @Test
    void getCurrentUser_ShouldReturnNull_WhenPrincipalIsAnonymousUser() {
        // Arrange
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.setContext(securityContext);
        // Act
        User currentUser = userService.getCurrentUser();
        // Assert
        assertNull(currentUser, "Expected null user when principal is anonymousUser");
    }

}