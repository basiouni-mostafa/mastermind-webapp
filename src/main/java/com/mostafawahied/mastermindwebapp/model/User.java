package com.mostafawahied.mastermindwebapp.model;

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
    @Column(unique = true)
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
    private boolean threeConsecutiveWinsAchievement = false; // 3 consecutive wins
    private boolean fiveConsecutiveWinsAchievement = false; // 5 consecutive wins
    private boolean streakChampionAchievement = false; // 10 consecutive wins
    private boolean newcomerAchievement = false; // 10 games played
    private boolean rookieAchievement = false; // 20 games played
    private boolean enthusiastAchievement = false; // 50 games played
    private boolean veteranAchievement = false; // 100 games played
    private boolean winnerAchievement = false; // 10 games won
    private boolean championAchievement = false; // 50 games won
    private boolean grandmasterAchievement = false; // 100 games won
    private boolean legendAchievement = false; // 200 games won
    private boolean mastermindAchievement = false; // 500 games won
    @ElementCollection
    private List<String> achievements = new ArrayList<>();

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
        checkForGamesPlayedAchievements().ifPresent(achievements::add);

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
            this.streakChampionAchievement = true;
            achievements.add("Streak Champion");
            return Optional.of("Streak Champion Achievement Unlocked!<br>10 Consecutive Wins! +200 extra points!");
        }
        return Optional.empty();
    }

    private Optional<String> checkForGamesPlayedAchievements() {
        if (this.gamesPlayed >= 10 && !this.newcomerAchievement) {
            this.newcomerAchievement = true;
            return Optional.of("Newcomer Achievement Unlocked! 10 Games Played!");
        } else if (this.gamesPlayed >= 20 && !this.rookieAchievement) {
            this.rookieAchievement = true;
            return Optional.of("Rookie Achievement Unlocked! 20 Games Played!");
        } else if (this.gamesPlayed >= 50 && !this.enthusiastAchievement) {
            this.enthusiastAchievement = true;
            return Optional.of("Enthusiast Achievement Unlocked! 50 Games Played!");
        } else if (this.gamesPlayed >= 100 && !this.veteranAchievement) {
            this.veteranAchievement = true;
            return Optional.of("Veteran Achievement Unlocked! 100 Games Played!");
        }
        return Optional.empty();
    }

    private Optional<String> checkForTotalWinsAchievements() {
        if (this.gamesWon >= 15 && !this.winnerAchievement) {
            this.winnerAchievement = true;
            return Optional.of("Winner Achievement Unlocked! 10 Wins!");
        } else if (this.gamesWon >= 50 && !this.championAchievement) {
            this.championAchievement = true;
            return Optional.of("Champion Achievement Unlocked! 50 Wins!");
        } else if (this.gamesWon >= 100 && !this.grandmasterAchievement) {
            this.grandmasterAchievement = true;
            return Optional.of("Grandmaster Achievement Unlocked! 100 Wins!");
        } else if (this.gamesWon >= 200 && !this.legendAchievement) {
            this.legendAchievement = true;
            return Optional.of("Legend Achievement Unlocked! 200 Wins!");
        } else if (this.gamesWon >= 500 && !this.mastermindAchievement) {
            this.mastermindAchievement = true;
            return Optional.of("Mastermind Achievement Unlocked! 500 Wins!");
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

    // Method to update achievements based on games played and won
    public void updateAchievements() {
        if (this.gamesPlayed >= 500 && !this.mastermindAchievement) {
            this.mastermindAchievement = true;
            achievements.add("Mastermind");
        }
        if (this.gamesPlayed >= 200 && !this.legendAchievement) {
            this.legendAchievement = true;
            achievements.add("Legend");
        }
        if (this.gamesPlayed >= 100 && !this.grandmasterAchievement) {
            this.grandmasterAchievement = true;
            achievements.add("Grandmaster");
        }
        if (this.gamesWon >= 50 && !this.championAchievement) {
            this.championAchievement = true;
            achievements.add("Champion");
        }
        if (this.gamesWon >= 15 && !this.winnerAchievement) {
            this.winnerAchievement = true;
            achievements.add("Winner");
        }
        if (this.gamesPlayed >= 100 && !this.veteranAchievement) {
            this.veteranAchievement = true;
            achievements.add("Veteran");
        }
        if (this.gamesPlayed >= 50 && !this.enthusiastAchievement) {
            this.enthusiastAchievement = true;
            achievements.add("Enthusiast");
        }
        if (this.gamesPlayed >= 20 && !this.rookieAchievement) {
            this.rookieAchievement = true;
            achievements.add("Rookie");
        }
        if (this.gamesPlayed >= 10 && !this.newcomerAchievement) {
            this.newcomerAchievement = true;
            achievements.add("Newcomer");
        }

//        winnerAchievement = false;

    }

    // Method to get the latest achievement
    public String getLatestAchievement() {
//        updateAchievements();
        if (!achievements.isEmpty()) {
            return achievements.get(achievements.size() - 1); // Return the last achievement in the list
        } else {
            return "Newbie"; // Return Newbie if the user has no achievements
        }
    }


}
