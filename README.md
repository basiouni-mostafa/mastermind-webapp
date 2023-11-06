## Project Overview

Click to play the game [here](https://mastermind-webapp-kst2.onrender.com/)

The Mastermind game webapp is a fun and engaging implementation of the classic game of Mastermind. It's built with Java
and Spring Boot for the backend, MySQL for the database, and the frontend is designed with Thymeleaf, Bootstrap, and
JavaScript. Players can compete against the computer, selecting different game modes and difficulty levels.
Users have the option
to create an account to track their scores and progress. The app also features a user profile page and a leaderboard
that ranks the top 10
players by score. The game is designed to be responsive and mobile-friendly.

## Technology Used

* Java
* Spring Boot
* Spring Security
* MySQL
* Docker
* Thymeleaf
* Bootstrap
* HTML
* CSS
* JavaScript

## Game Extensions

* User authentication with Spring Security, MySQL, and OAuth2.
* Google account sign-in integration.
* User profile page displaying individual statistics.
* Achievement badges.
* Leaderboard for top scores.
* New 'Colors' game mode alongside the traditional 'Numbers'.
* Difficulty levels (Easy, Medium, Hard) with customizable combination length and time limits.
* Points-based scoring system.
* Difficulty-adjusted timers.
* Hint feature to aid in solving combinations.
* Modern design and animations to enhance user experience
* Deployment using Docker and Render

## Installation and Running the Game

### Live Website

Click to play the game [here](https://mastermind-webapp-kst2.onrender.com/)

### Running the Game Locally

#### Prerequisites:

* Java 8 or higher
* Maven
* MySQL Workbench

#### Instructions:

1. Clone the repository
2. Open the project in your IDE
3. Create a new database in MySQL Workbench
4. Update the database connection details in the '**application.properties**'.
5. Run the project
6. Navigate to http://localhost:8080/
7. Enjoy the game!

## Game Play

1. Play as a guest or register to track scores and achievements.
2. Start a new game by selecting difficulty and game type in settings.
3. Guess the combination of numbers or colors, receiving feedback for correct guesses and correct positions.
4. Win by correctly guessing the entire combination.
5. Use hints if needed. This will reveal a guess you haven't tried yet.
6. Game ends after a correct guess, running out of turns, or time.
7. Scores are tracked for registered users (score is based on the difficulty level).
8. View your profile and leaderboard standings.

## Code Structure

The game is built on Spring boot framework and implements the Model-View-Controller (MVC) pattern.

* The '**controller**' package: Manages HTTP requests and routing.
* The '**manager**' package: Handles game logic, combination generation, scoring, and user interactions.
* The '**model**'  package: Contains game state, types, guess history, and other game-related data.
* The '**view**' package: Includes Thymeleaf templates for the frontend.
