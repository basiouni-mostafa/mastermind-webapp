## Project Overview

You can check the live website [here](https://mastermind-webapp-kst2.onrender.com/)

The Mastermind game web app is a fun and engaging implementation of the classic game of Mastermind. The game is built using Java and Spring Boot, and the front-end is built using Thymeleaf, Bootstrap, and JavaScript. The game is played against the computer, and the user can choose the difficulty level. The game is designed to be responsive and mobile-friendly.


## Technology Used
* Java | Spring Boot | Docker | Thymeleaf | Bootstrap | HTML | CSS | JavaScript

## Game Extensions

* Added login and registration functionality
* Added login in using Google
* Added a user profile page
* Added a leaderboard page
* Added a new game type (Colors) in addition to the classic game type (Numbers)
* Added difficulty level of (Easy, Medium, Hard) with the option to configure the combination length and guessing time.
* Added score tracking using a points system based on each difficulty level
* Added a timer for each game that changes based on the difficulty level
* Added hint support
* Added an intro and animations to enhance user experience
* Deployed using Docker

## Installation and Running the Game

### Live Website

You can visit the live website [here](https://mastermind-webapp-kst2.onrender.com/)

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

1. Select your difficulty level
2. You will be prompted to guess the combination of numbers or colors based on the game type
3. You will get feedback on each guess for the correct guesses and the correct locations of your guesses
4. The game is won when you correctly guess the combination
5. You can use a hint when you are stuck
6. The game ends when you correctly guess the combination, when you run out of turns or when you run out of time (if the game difficulty level is set to Medium or Hard)
7. The game will track your score (wins and losses)
8. The timer for the game is based on the difficulty level you select and will be shown in minutes

## Code Structure
The game is built on Spring boot framework and implements the Model-View-Controller (MVC) pattern.

* The 'controller' package handles all the HTTP requests and routes them to the appropriate services.
* The 'manager' package handles the game logic, generating random combinations, scoring, handling user input and other game related tasks.
* The 'model' package contains the game state, game type, guess history and other game-related data.
* The 'view' package contains all the Thymeleaf templates for the front-end.
* The 'static' folder contains all the images, CSS and JavaScript files.

## Conclusion
This project is a fun and engaging implementation of the classic game of Mastermind with configurable modes and added features that enhances
the user experience. This can be run on any system with the necessary software installed and can be easily modified to
suit any specific needs.
