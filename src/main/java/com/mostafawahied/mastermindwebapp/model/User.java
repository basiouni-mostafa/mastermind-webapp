package com.mostafawahied.mastermindwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private String role = "ROLE_USER";
    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthenticationProvider authProvider;
    private String resetPasswordToken;
    private int score = 0;
    private int consecutiveWins = 0;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void addScore(int points) {
        this.score += points;
        this.score = Math.max(this.score, 0);
    }

    public void subtractScore(int points) {
        this.score -= points;
        this.score = Math.max(this.score, 0);
    }

    public Optional<String> addConsecutiveWin() {
        this.consecutiveWins++;
        if (this.consecutiveWins == 3) {
            this.addScore(50);
            return Optional.of("3 Consecutive Wins! +50 points!");
        } else if (this.consecutiveWins == 5) {
            this.addScore(100);
            return Optional.of("5 Consecutive Wins! +100 points!");
        } else if (this.consecutiveWins == 10) {
            this.addScore(200);
            return Optional.of("10 Consecutive Wins! +200 points!");
        }
        return Optional.empty();
    }

    public void resetConsecutiveWins() {
        this.consecutiveWins = 0;
    }

    public Optional<String> updateScore(Game game) {
        if (game.getGameState() == GameState.WON) {
            this.addScore(game.getGameDifficulty().winPoints);
            return this.addConsecutiveWin();
        } else if (game.getGameState() == GameState.LOST) {
            this.subtractScore(game.getGameDifficulty().losePoints);
            this.resetConsecutiveWins();
        }
        return Optional.empty();
    }

//    public boolean updateScore(Game game) {
//        boolean bonusWon = false;
//        if (game.getGameState() == GameState.WON) {
//            this.addScore(game.getGameDifficulty().winPoints);
//            bonusWon = this.addConsecutiveWin();
//        } else if (game.getGameState() == GameState.LOST) {
//            this.subtractScore(game.getGameDifficulty().losePoints);
//            this.resetConsecutiveWins();
//        }
//        return bonusWon;
//    }

}
