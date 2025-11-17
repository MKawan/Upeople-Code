package unit01.programmingAssignment;
/**
 *
 * @author mk
 */
import java.util.*;
import unit01.programmingAssignment.model.AdvancedTextProcessor;
import unit01.programmingAssignment.model.TextProcessor;
/**
 * Main class - Responsible for:
 * 1. Displaying welcome message
 * 2. Getting text input from user
 * 3. Coordinating all analysis operations
 * 4. Displaying results in a user-friendly way
 */
public class TextAnalyzer {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=".repeat(60));
            System.out.println("     WELCOME TO THE TEXT ANALYSIS TOOL");
            System.out.println("=".repeat(60));
            System.out.println("This tool will analyze your text and provide detailed statistics.\n");
            
            // Get paragraph from user with validation
            String text = getValidParagraph(scanner);
            
            // Create processor instance (polymorphism via inheritance)
            TextProcessor processor = new AdvancedTextProcessor(text);
            
            // Perform and display all analyses
            processor.displayBasicStats();
            processor.handleCharacterFrequencyQuery(scanner);
            processor.handleWordFrequencyQuery(scanner);
            processor.displayUniqueWordCount();
            
            System.out.println("\nThank you for using the Text Analysis Tool!");
            System.out.println("=".repeat(60));
        }
    }
    
    /**
     * Ensures the user enters a non-empty paragraph with at least 10 characters
     */
    private static String getValidParagraph(Scanner scanner) {
        String text;
        do {
            System.out.println("Please enter a paragraph or lengthy text (minimum 10 characters):");
            text = scanner.nextLine().trim();
            
            if (text.isEmpty()) {
                System.out.println("Error: Input cannot be empty. Please try again.\n");
            } else if (text.length() < 10) {
                System.out.println("Error: Text is too short. Please enter at least 10 characters.\n");
            }
        } while (text.isEmpty() || text.length() < 10);

        System.out.println("\nText accepted! Analyzing...\n");
        return text;
    }
}