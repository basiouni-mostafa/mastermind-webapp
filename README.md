## Project Overview

The Mastermind game web app that I created is an implementation of the classic game of Mastermind. It was built using the
Spring Boot framework.

**You can now view the live website Deployed on AWS through this [link](http://mastermindwebapp-env.eba-rtrpmb6e.us-east-2.elasticbeanstalk.com/)**

## Technology Used
* Java | Spring Boot | Thymeleaf | Bootstrap | HTML | CSS | JavaScript

## Game Extensions

* Deployed on AWS using Elastic Beanstalk
* Added hint support
* Added a Colors game mode
* Added difficulty level of (Easy, Medium, Hard) with the option to configure the combination length and guessing time.
* Added an intro and animations to enhance user experience
* Added score tracking for number of wins and number of losses
* Added a timer for each game that changes based on the difficulty level

## Installation and Running the Game

1. Make sure you have the following installed on your system:

* Java 8 or higher
* Maven

2. Clone or download the repository 'https://github.com/Mostafa-Wahied/mastermind-webapp'
3. Navigate to the project directory in your terminal
4. Run the command 'mvn clean install' to build and install the project
5. Start the application by running the command 'mvn spring-boot:run'
6. The game should now be running on 'http://localhost:8080' in your browser

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
