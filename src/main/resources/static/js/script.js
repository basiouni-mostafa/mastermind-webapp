// Handle adding digits to inputs
function addDigit(digit) {
    const inputs = document.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].value.length === 0) {
            inputs[i].value = digit;
            break;
        }
    }
}

// End of handling adding digits


// Handle adding colors to inputs
function addColor(color) {
    // Select the input elements
    const inputs = document.querySelectorAll("input[name='userGuesses']");
    // Set the background color of each input element
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].classList.length === 0) {
            inputs[i].classList.add(`${color}`);
            // Set the text color to transparent
            inputs[i].style.color = 'transparent';
            // ↓ !Important - Assign the value to the name of the color
            // because without this the color strings wouldn't be passed to the serverside method
            inputs[i].value = color;
            break;
        }
    }
}

// End of handling adding colors

// Stop the form from submitting if all inputs are not filled
function addFormEventListener() {
    const form = document.getElementById("guess-form");
    form.addEventListener("submit", event => {
        // Select the input elements
        const inputs = document.querySelectorAll("input[name='userGuesses']");

        // Check if any of the inputs are empty
        const isEmpty = Array.from(inputs).some(input => input.value === "");

        // If any of the inputs are empty, prevent the form from being submitted
        // and show the error message
        if (isEmpty) {
            // Show an error message to fill all inputs before submitting
            const p = document.createElement("p");
            p.textContent = "Please fill out all the inputs before submitting.";
            p.style.color = "red";
            form.appendChild(p);
            event.preventDefault();
        }
    });
}

// I had to wrap it in a function "addFormEventListener()" and
// call it when the DOM content is loaded because of a null return error
document.addEventListener("DOMContentLoaded", addFormEventListener);
// End of form submitting handling

// Game settings modal
function addSettingsModalEventListener() {
    const settingsIcon = document.getElementById('settings-icon');
    const gameSettings = document.getElementById('game-settings');
    const closeSettings = document.getElementById('close-settings');
    // Open the settings if btn is clicked and close if user clicked anywhere else
    document.addEventListener('click', (event) => {
        if (event.target.id === 'settings-icon') {
            // First check whether the modal is already open or closed
            if (gameSettings.style.display === 'block') {
                gameSettings.style.display = 'none';
            } else {
                gameSettings.style.display = 'block';
            }
        } else {
            gameSettings.style.display = 'none';
        }
    });
    // exclude the game settings modal from the area that will close the modal
    gameSettings.addEventListener('click', function (event) {
        event.stopPropagation();
    });
    // close the modal if the close btn is clicked
    closeSettings.addEventListener('click', () => {
        gameSettings.style.display = 'none';
    });
}

// I had to wrap it in a function "addFormEventListener()" and
// call it when the DOM content is loaded because of a null return error
document.addEventListener("DOMContentLoaded", addSettingsModalEventListener);
// End of game settings modal

// Game rules modal
function addModalEventListener() {
    const settingsIcon = document.getElementById('rules-icon');
    const gameSettings = document.getElementById('game-rules');
    const closeSettings = document.getElementById('close-rules');
    // Open the settings if btn is clicked and close if user clicked anywhere else
    document.addEventListener('click', (event) => {
        if (event.target.id === 'rules-icon') {
            // First check whether the modal is already open or closed
            if (gameSettings.style.display === 'block') {
                gameSettings.style.display = 'none';
            } else {
                gameSettings.style.display = 'block';
            }
        } else {
            gameSettings.style.display = 'none';
        }
    });
    // exclude the game settings modal from the area that will close the modal
    gameSettings.addEventListener('click', function (event) {
        event.stopPropagation();
    });
    // close the modal if the close btn is clicked
    closeSettings.addEventListener('click', () => {
        gameSettings.style.display = 'none';
    });
}

// I had to wrap it in a function "addFormEventListener()" and
// call it when the DOM content is loaded because of a null return error
document.addEventListener("DOMContentLoaded", addModalEventListener);
// End of games rules modal

// Removing the lost animation after it's done
function removeAnimation() {
    const animation = document.getElementById('lost-animation');
    animation.addEventListener('complete', () => {
        const container = animation.closest('.lose-animation');
        container.remove();
    });
}

document.addEventListener("DOMContentLoaded", removeAnimation)
// End of removing lost animation

// Intro animation
const intro_left = document.getElementById("intro-window-left");
const intro_right = document.getElementById("intro-window-right");
const intro_text_1 = document.getElementById("intro-text-1");
const intro_text_2 = document.getElementById("intro-text-2");
const intro_button = document.getElementById("intro-button");
// Store whether the animation played before in session storage
// this gets deleted when the window or tab closes
const animationPlayed = sessionStorage.getItem("animationPlayed");
// Check if the animation played before
if (animationPlayed !== "true") {
    // If not, show the elements
    intro_left.style.display = "block";
    intro_right.style.display = "block";
    intro_text_1.style.display = "block";
    intro_text_2.style.display = "block";
    intro_button.style.display = "block";
}
// Function to trigger the animation with the button click and hides the elements
// and removes them from the dom so they don't take up space
function closeIntro() {
    // run the animation
    intro_left.classList.add('animate__slideOutLeft');
    intro_right.classList.add('animate__slideOutRight');
    intro_text_1.style.display = "none";
    intro_text_2.style.display = "none";
    intro_button.style.display = "none";
    intro_left.addEventListener("animationend", function () {
        this.remove();
    });
    intro_right.addEventListener("animationend", function () {
        this.remove();
    });
    sessionStorage.setItem("animationPlayed", "true");
}
// End of intro animation