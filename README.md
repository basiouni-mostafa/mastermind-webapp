## Project Overview

Click here to play the game [here](https://mastermind-webapp-kst2.onrender.com/)

The Mastermind game web app is a fun and engaging implementation of the classic game of Mastermind. The game is built
using Java, Spring Boot and MySQL, and the front-end is built using Thymeleaf, Bootstrap, and JavaScript. The game is
played against the computer, and the user can choose the difficulty level. The user can create an account and login to
track their score and achievements. There is also a leaderboard that displays the top 10 users based on their score. The
game is designed to be responsive and mobile-friendly.

## Technology Used

* Java | Spring Boot | MySQL | Docker | Thymeleaf | Bootstrap | HTML | CSS | JavaScript

## Game Extensions

* Added login and registration functionality using Spring Security, MySQL and oAuth2
* Added login in using Google account
* Added a user profile page that displays the user's statistics
* Added badges for the user's achievements
* Added a leaderboard page that displays the top 10 users based on their score
* Added a new game type (Colors) in addition to the classic game type (Numbers)
* Added difficulty level of (Easy, Medium, Hard) with the option to configure the combination length and guessing time.
* Added score tracking using a points system based on each difficulty level
* Added a timer for that corresponds to the difficulty level
* Added hint support that reveals a random number or color in the combination that the user hasn't guessed yet
* Added an intro and animations to enhance user experience
* Deployed using Docker and Render

## Installation and Running the Game

### Live Website

Click here to play the game [here](https://mastermind-webapp-kst2.onrender.com/)

### Running the Game Locally

1. Make sure you have the following installed on your system:

* Java 8 or higher
* Maven
* MySQL Workbench

2. Clone the repository
3. Open the project in your IDE
4. Create a new database in MySQL Workbench
5. Update the database connection details in the application.properties file
6. Run the project
7. Open your browser and go to http://localhost:8080/
8. Enjoy the game!

## Game Play

1. You can choose to play as a guest or create an account and login to track your score and achievements.
2. To start a new game, click on the settings icon and select your difficulty level and game type.
3. You will be prompted to guess the combination of numbers or colors based on the game type.
4. You will get feedback on each guess for the correct guesses and the correct locations of your guesses.
5. The game is won when you correctly guess the combination.
6. You can use a hint when you are stuck.
7. The game ends when you correctly guess the combination, when you run out of turns or when you run out of time (if the
   game difficulty level is set to Medium or Hard).
8. If you are logged in, the game will track your score (score is based on the difficulty level).
9. The timer for the game is based on the difficulty level you select and will be shown in minutes.
10. You can view your profile page by clicking on the profile icon and clicking on your username.
11. You can view the leaderboard by clicking on the leaderboard button.
12. You can log out by clicking on the logout button under your username.
13. You can view the rules of the game by clicking on the rules button.


## Code Structure

The game is built on Spring boot framework and implements the Model-View-Controller (MVC) pattern.

* The 'controller' package handles all the HTTP requests and routes them to the appropriate services.
* The 'manager' package handles the game logic, generating random combinations, scoring, handling user input and other
  game related tasks.
* The 'model' package contains the game state, game type, guess history and other game-related data.
* The 'view' package contains all the Thymeleaf templates for the front-end.
* The 'static' folder contains all the images, CSS and JavaScript files.
