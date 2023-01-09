// handle adding digits to inputs
function addDigit(digit) {
    const inputs = document.getElementsByTagName("input");
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].value.length === 0) {
            inputs[i].value = digit;
            break;
        }
    }
}

// handle adding colors to inputs
function addColor(color) {
    // Select the input elements
    const inputs = document.querySelectorAll("input[name='userGuesses']");
    // Set the background color of each input element
    for (let i = 0; i < inputs.length; i++) {
        if (inputs[i].style.backgroundColor === "") {
            inputs[i].style.backgroundColor = color;
            // inputs[i].style.background = `linear-gradient(to bottom, ${color}, rgba(0, 0, 0, 0.9))`;
            // Set the text color to transparent
            inputs[i].style.color = 'transparent';
            // ↓ !Important - Assign the value to the name of the color
            // because without this the color strings wouldn't be passed to the serverside method
            inputs[i].value = color;
            break;
        }
    }
}

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

