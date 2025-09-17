package unit.pkg01;
/**
 * @author mk
 */
// Java program to simulate a quiz game with 10 Java programming questions
import java.util.Scanner;

public class QuizGame {
    public static void main(String[] args) {
        // Initialize Scanner for user input
        Scanner scanner = new Scanner(System.in);
        
        // Array to store Java programming questions
        String[] questions = {
            "Which Java keyword is used to prevent method overriding?\nA) static\nB) final\nC) abstract\nD) synchronized",
            "What is the default value of an uninitialized instance variable of type int in Java?\nA) 0\nB) null\nC) -1\nD) undefined",
            "Which Java collection class guarantees no duplicate elements and maintains natural ordering?\nA) ArrayList\nB) HashMap\nC) TreeSet\nD) LinkedList",
            "What is the output of System.out.println(1 + 2 + \"3\") in Java?\nA) 33\nB) 6\nC) 123\nD) Compilation error",
            "Which exception is thrown when trying to access an array index that does not exist?\nA) NullPointerException\nB) ArrayIndexOutOfBoundsException\nC) IllegalArgumentException\nD) ClassCastException",
            "Which access modifier makes a method accessible only within the same package unless inherited?\nA) public\nB) private\nC) protected\nD) default",
            "What is the purpose of the 'super' keyword in Java?\nA) Calls a superclass method or constructor\nB) Declares a static method\nC) Initializes an array\nD) Creates a new object",
            "Which Java interface is used to make objects comparable for sorting?\nA) Serializable\nB) Comparable\nC) Comparator\nD) Cloneable",
            "What is the result of compiling a Java source file?\nA) Machine code\nB) Bytecode\nC) Executable file\nD) Assembly code",
            "Which loop is guaranteed to execute at least once in Java?\nA) for\nB) while\nC) do-while\nD) foreach"
        };
        
        // Array to store correct answers
        char[] correctAnswers = {'B', 'A', 'C', 'A', 'B', 'D', 'A', 'B', 'B', 'C'};

        // Variable to track the number of correct answers
        int correctCount = 0;
        int totalQuestions = questions.length;
        
        // Welcome message
        System.out.println("Welcome to the Java Programming Quiz Game!");
        System.out.println("Answer each question by typing A, B, C, or D.\n");
        
        // Loop through each question
        for (int i = 0; i < totalQuestions; i++) {
            // Display the current question
            System.out.println("Question " + (i + 1) + ":");
            System.out.println(questions[i]);
            System.out.print("Your answer: ");
            
            // Read user input and convert to uppercase
            String input = scanner.nextLine().trim().toUpperCase();
            
            // Input validation: ensure the answer is A, B, C, or D
            if (input.length() == 1 && (input.charAt(0) == 'A' || input.charAt(0) == 'B' || 
                                        input.charAt(0) == 'C' || input.charAt(0) == 'D')) {
                // Use switch to compare user answer with the correct answer
                switch (input.charAt(0)) {
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                        // Check if the answer is correct
                        if (input.charAt(0) == correctAnswers[i]) {
                            System.out.println("Correct!");
                            correctCount++;
                        } else {
                            System.out.println("Incorrect. The correct answer is " + correctAnswers[i] + ".");
                        }
                        break;
                }
            } else {
                // Handle invalid input
                System.out.println("Invalid input! Please enter A, B, C, or D.");
                i--; // Repeat the current question
            }
            System.out.println(); // Blank line for readability
        }
        
        // Calculate and display the final score as a percentage
        double scorePercentage = (double) correctCount / totalQuestions * 100;
        System.out.printf("Quiz completed! Your score: %d/%d (%.2f%%)%n", 
                          correctCount, totalQuestions, scorePercentage);
        
        // Provide feedback based on the score using if statements
        if (scorePercentage == 100) {
            System.out.println("Excellent! Perfect score!");
        } else if (scorePercentage >= 60) {
            System.out.println("Good job! You passed!");
        } else {
            System.out.println("Try again to improve your score!");
        }
        
        // Close the scanner
        scanner.close();
    }
}