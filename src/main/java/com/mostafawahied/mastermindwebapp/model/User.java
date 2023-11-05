package com.mostafawahied.mastermindwebapp.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
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
    private int gamesPlayed = 0;
    private int gamesWon = 0;
    private int gamesLost = 0;
    private String profileImageSrc = "/image/user.png";
    private int mostConsecutiveWins = 0;
    private int threeConsecutiveWinsCount = 0;
    private int fiveConsecutiveWinsCount = 0;
    private int tenConsecutiveWinsCount = 0;
    private boolean StreakChampionAchievement = false;
    private boolean veteranAchievement = false;
    private boolean mastermindAchievement = false;

    private static final int THREE_CONSECUTIVE_WINS = 3;
    private static final int FIVE_CONSECUTIVE_WINS = 5;
    private static final int TEN_CONSECUTIVE_WINS = 10;

    private static final int REWARD_3_CONSECUTIVE_WINS = 50;
    private static final int REWARD_5_CONSECUTIVE_WINS = 100;
    private static final int REWARD_10_CONSECUTIVE_WINS = 200;

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

    public Optional<String> checkAchievements() {
        List<String> achievements = new ArrayList<>();
        checkForConsecutiveWinsAchievements().ifPresent(achievements::add);
        checkForTotalWinsAchievements().ifPresent(achievements::add);
        return achievements.isEmpty() ? Optional.empty() : Optional.of(String.join(" ", achievements));
    }

    private Optional<String> checkForConsecutiveWinsAchievements() {
        if (this.consecutiveWins == THREE_CONSECUTIVE_WINS) {
            this.addScore(REWARD_3_CONSECUTIVE_WINS);
            this.threeConsecutiveWinsCount++;
            return Optional.of("3 Consecutive Wins! +50 extra points!");
        } else if (this.consecutiveWins == FIVE_CONSECUTIVE_WINS) {
            this.addScore(REWARD_5_CONSECUTIVE_WINS);
            this.fiveConsecutiveWinsCount++;
            return Optional.of("5 Consecutive Wins! +100 extra points!");
        } else if (this.consecutiveWins == TEN_CONSECUTIVE_WINS) {
            this.addScore(REWARD_10_CONSECUTIVE_WINS);
            this.tenConsecutiveWinsCount++;
            this.StreakChampionAchievement = true;
            return Optional.of("Streak Champion Achievement Unlocked! 10 Consecutive Wins! +200 extra points!");
        }
        return Optional.empty();
    }

    private Optional<String> checkForTotalWinsAchievements() {
        if (this.gamesWon >= 50 && !this.veteranAchievement) {
            this.veteranAchievement = true;
            return Optional.of("Veteran Achievement Unlocked! 50 Wins!");
        } else if (this.gamesWon >= 100 && !this.mastermindAchievement) {
            this.mastermindAchievement = true;
            return Optional.of("Mastermind Achievement Unlocked! 100 Wins!");
        }
        return Optional.empty();
    }

    public Optional<String> recordWin(Game game) {
        this.gamesPlayed++;
        this.gamesWon++;
        this.addScore(game.getGameDifficulty().winPoints);
        this.consecutiveWins++;
        this.mostConsecutiveWins = Math.max(this.mostConsecutiveWins, this.consecutiveWins);
        return this.checkAchievements();
    }

    public Optional<String> recordLoss(Game game) {
        this.gamesPlayed++;
        this.gamesLost++;
        this.subtractScore(game.getGameDifficulty().losePoints);
        this.resetConsecutiveWins();
        return Optional.empty();
    }

    public void resetConsecutiveWins() {
        this.consecutiveWins = 0;
    }

    public double getWinPercentage() {
        if (this.gamesPlayed == 0) {
            return 0;
        }
        return Math.round((double) this.gamesWon / this.gamesPlayed);
    }

    public double getAverageScore() {
        if (this.gamesPlayed == 0) {
            return 0;
        }
        return Math.round((double) this.score / this.gamesPlayed * 100.0) / 100.0;
    }

}
