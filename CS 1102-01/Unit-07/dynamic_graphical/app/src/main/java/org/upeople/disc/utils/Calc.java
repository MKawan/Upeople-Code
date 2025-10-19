package org.upeople.disc.utils;

import javafx.scene.control.TextField;

/**
 * Utility class containing the calculator logic.
 * This class handles button clicks and performs basic arithmetic operations.
 */
public class Calc {

    private static double num1 = 0;   // First operand
    private static String operator = ""; // Operator selected (+, -, *, /)
    private static boolean start = true; // Flag to clear display on new input

    /**
     * Handles button clicks from the GUI.
     * @param text The text of the button clicked
     * @param display The TextField displaying input/results
     */
    public static void handleButton(String text, TextField display) {
        switch (text) {
            case "C": // Clear all inputs and reset state
                display.clear();
                operator = "";
                start = true;
                num1 = 0;
                break;
            case "+": case "-": case "*": case "/": // Set operator and store first number
                if (!operator.isEmpty()) {
                    return; // Prevent double operator input
                }
                operator = text;
                num1 = Double.parseDouble(display.getText());
                start = true;
                break;
            case "=": // Perform calculation
                if (operator.isEmpty()) return;
                double num2 = Double.parseDouble(display.getText());
                double result = calculate(num1, num2, operator);
                display.setText(String.valueOf(result));
                operator = "";
                start = true;
                break;
            default: // Numbers
                if (start) {
                    display.clear();
                    start = false;
                }
                display.appendText(text);
                break;
        }
    }

    /**
     * Performs basic arithmetic operations.
     * @param a First operand
     * @param b Second operand
     * @param op Operator (+, -, *, /)
     * @return Result of operation
     */
    private static double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> b != 0 ? a / b : 0; // Avoid division by zero
            default -> 0;
        };
    }
}
