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
// when the document is loaded
function addFormEventListener() {
    const form = document.getElementById("guess-form");
    if (form === null) {
        return;
    }
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
    if (settingsIcon === null) {
        return;
    }
    if (gameSettings === null) {
        return;
    }
    if (closeSettings === null) {
        return;
    }
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
            if (gameSettings !== null) {
                gameSettings.style.display = 'none';
            }
        }
    });
    // exclude the game settings modal from the area that will close the modal
    if (gameSettings !== null) {

        gameSettings.addEventListener('click', function (event) {
            event.stopPropagation();
        });
    }
    // close the modal if the close btn is clicked
    if (closeSettings !== null) {
        closeSettings.addEventListener('click', () => {
            gameSettings.style.display = 'none';
        });
    }
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
    if (settingsIcon === null) {
        return;
    }
    if (gameSettings === null) {
        return;
    }
    if (closeSettings === null) {
        return;
    }
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
            if (gameSettings !== null) {
                gameSettings.style.display = 'none';
            }
        }
    });
    // exclude the game settings modal from the area that will close the modal
    if (gameSettings !== null) {
        gameSettings.addEventListener('click', function (event) {
            event.stopPropagation();
        });
    }

// close the modal if the close btn is clicked
    if (closeSettings !== null) {
        closeSettings.addEventListener('click', () => {
            gameSettings.style.display = 'none';
        });
    }
}

// I had to wrap it in a function "addFormEventListener()" and
// call it when the DOM content is loaded because of a null return error
document.addEventListener("DOMContentLoaded", addModalEventListener);
// End of games rules modal

// Removing the lost animation after it's done
function removeAnimation() {
    const animation = document.getElementById('lost-animation');
    if (animation === null) {
        return;
    }
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

// consecutive wins notification
document.addEventListener('DOMContentLoaded', function () {
    const notification = document.getElementById('notification');
    if (notification) {
        setTimeout(() => {
            notification.classList.add('hidden'); // Add the "hidden" class to make it fade
        }, 3000);
    }
});
// End of consecutive wins notification

// handle delete button
function deleteLastInput() {
    const inputs = document.querySelectorAll("input[name='userGuesses']");
    // Start from the end of the inputs list and find the last one with a value
    for (let i = inputs.length - 1; i >= 0; i--) {
        if (inputs[i].value !== "") {
            inputs[i].value = ""; // Clear the value
            inputs[i].classList.remove(...inputs[i].classList); // Remove all classes (including color)
            inputs[i].style.color = ''; // Reset the text color
            break; // Exit the loop after clearing the last filled input
        }
    }
}

// End of handle delete button

// add event listener to delete button digit
document.addEventListener("DOMContentLoaded", () => {
    const deleteBtnDigit = document.querySelector('.delete-btn-digit');
    if (deleteBtnDigit === null) {
        return;
    }
    deleteBtnDigit.addEventListener('click', deleteLastInput);
});

// add event listener to delete button color
document.addEventListener("DOMContentLoaded", () => {
    const deleteBtnColor = document.querySelector('.delete-btn-color');
    if (deleteBtnColor === null) {
        return;
    }
    deleteBtnColor.addEventListener('click', deleteLastInput);
});
// End of add event listener to delete button color

// handle share button
// document.addEventListener('DOMContentLoaded', (event) => {
//     const isMobile = document.body.getAttribute('data-is-mobile') === 'true';
//
//     if (document.getElementById('shareButton') === null) {
//         return;
//     }
//     document.getElementById('shareButton').addEventListener('click', () => {
//         const gameSummary = document.getElementById('gameSummary').textContent;
//         console.log("gameSummary = " + gameSummary);
//
//         if (isMobile && navigator.share) {
//             navigator.share({
//                 text: gameSummary
//             }).then(() => {
//                 showNotification('Game summary shared successfully!');
//             }).catch((error) => {
//                 console.error('Error sharing:', error);
//                 copyTextToClipboard(gameSummary);
//             });
//         } else {
//             copyTextToClipboard(gameSummary);
//         }
//     });
//
//     function copyTextToClipboard(text) {
//         console.log("copyTextToClipboard function called");
//         const textArea = document.createElement('textarea');
//         textArea.value = text;
//         document.body.appendChild(textArea);
//         textArea.select();
//         document.execCommand('copy');
//         document.body.removeChild(textArea);
//
//         showNotification('Game summary copied to clipboard!');
//     }
//
//     function showNotification(message) {
//         console.log("showNotification function called");
//         const notification = document.getElementById('copyNotification');
//         console.log("notification = " + notification);
//         if (notification) {
//             notification.textContent = message;
//             notification.style.display = 'block';
//
//             setTimeout(() => {
//                 notification.style.display = 'none';
//             }, 3000); // Hide after 3 seconds
//         }
//     }
// });
// End of handle share button

function handleShare(gameSummary) {
    const isMobile = document.body.getAttribute('data-is-mobile') === 'true';
    console.log("handleShare function called");
    try {
        console.log("Device is mobile");
        if (isMobile && navigator.share) {
            console.log("navigator.share is available");
            navigator.share({
                text: gameSummary
            }).then(() => {
                showNotification('Game summary shared successfully!');
            }).catch((error) => {
                console.error('Error sharing:', error);
                copyTextToClipboard(gameSummary);
            });
        } else {
            console.log("navigator.share is not available");
            // testNavigatorShare();
            copyTextToClipboard(gameSummary);
        }
    } catch (error) {
        console.error('Unhandled error:', error);
    }
}

function copyTextToClipboard(text) {
    console.log("copyTextToClipboard function called");
    const textArea = document.createElement('textarea');
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.select();
    document.execCommand('copy');
    document.body.removeChild(textArea);

    showNotification('Game summary copied to clipboard!');
}

function showNotification(message) {
    console.log("showNotification function called");
    const notification = document.getElementById('copyNotification');
    console.log("notification = " + notification);
    if (notification) {
        notification.textContent = message;
        notification.style.display = 'block';

        setTimeout(() => {
            notification.style.display = 'none';
        }, 3000); // Hide after 3 seconds
    }
}

// Simplified Test for navigator.share
function testNavigatorShare() {
    console.log(`TEST navigator.share: ${typeof navigator.share}`);
    if (navigator.share) {
        console.log('TEst Attempting to use navigator.share');
        navigator.share({ text: 'Test share' })
            .then(() => console.log('TEst Share was successful'))
            .catch((error) => console.error(`TEst Share failed: ${error}`));
    } else {
        console.log('TEst navigator.share is not available');
    }
}

// Event listener for the share button
document.getElementById('shareButton').addEventListener('click', () => {
    const gameSummary = document.getElementById('gameSummary').textContent;
    if (gameSummary === null) {
        return;
    }
    console.log("gameSummary = " + gameSummary);
    handleShare(gameSummary);
});



// share this

// Function to update ShareThis configuration
window.__sharethis__ = undefined;

function updateShareData(gameSummary) {
    // Assuming gameSummary is a string you want to share
    const shareConfig = {
        url: window.location.href, // URL to share
        title: 'Check out my game summary!', // Title for sharing
        description: gameSummary, // Description for sharing
        // image: 'path_to_image_if_any.jpg' // Image for sharing
    };

    // Initialize ShareThis with the new configuration
    window.__sharethis__.load('inline-share-buttons', shareConfig);
}

// Example of triggering the update
document.addEventListener('DOMContentLoaded', function() {
    // Get gameSummary from the model attribute
    const gameSummary = document.getElementById('gameSummary').textContent;
    updateShareData(gameSummary);
});
// End of share this