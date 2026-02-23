// Add event listener to the button for dynamic behavior
document.getElementById('convertButton').addEventListener('click', performConversion);

// Main function for conversion and validation
function performConversion() {
    const inputValue = document.getElementById('tempInput').value;
    const selectedUnit = document.getElementById('unitSelect').value;
    const resultDiv = document.getElementById('resultDisplay');
    const errorDiv = document.getElementById('errorDisplay');

    // Clear previous displays
    resultDiv.textContent = '';
    errorDiv.textContent = '';

    // Validation: check if input is numeric using if
    if (isNaN(inputValue) || inputValue === '') {
        errorDiv.textContent = 'Invalid input: please enter a number.';
        return;
    }

    const temperature = parseFloat(inputValue);
    let convertedTemp;

    // Control structure for conversion based on selection
    if (selectedUnit === 'cToF') {
        // Formula: F = (C × 9/5) + 32
        convertedTemp = (temperature * 9 / 5) + 32;
        resultDiv.textContent = `${temperature}°C is ${convertedTemp.toFixed(2)}°F`;
    } else if (selectedUnit === 'fToC') {
        // Formula: C = (F - 32) × 5/9
        convertedTemp = (temperature - 32) * 5 / 9;
        resultDiv.textContent = `${temperature}°F is ${convertedTemp.toFixed(2)}°C`;
    } else {
        errorDiv.textContent = 'Please select a valid unit.';
    }
}