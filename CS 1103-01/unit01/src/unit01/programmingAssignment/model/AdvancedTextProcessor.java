package unit01.programmingAssignment.model;

import java.util.Scanner;
/**
 *
 * @author mk
 */
public class AdvancedTextProcessor extends TextProcessor {

    public AdvancedTextProcessor(String text) {
        super(text);
    }

    @Override
    public void displayBasicStats() {
        System.out.println("BASIC TEXT STATISTICS");
        System.out.println("-".repeat(40));
        System.out.printf("Total characters   : %,d%n", countCharacters());
        System.out.printf("Total words        : %,d%n", countWords());
        System.out.printf("Most common char   : '%c'%n", findMostCommonCharacter());
        System.out.println();
    }

    @Override
    public void handleCharacterFrequencyQuery(Scanner scanner) {
        System.out.print("Enter a character to search its frequency: ");
        String input = scanner.nextLine().trim();

        while (input.isEmpty() || input.length() > 1) {
            System.out.print("Please enter exactly one character: ");
            input = scanner.nextLine().trim();
        }

        char target = Character.toLowerCase(input.charAt(0));
        int count = 0;
        for (char c : lowercaseText.toCharArray()) {
            if (c == target) count++;
        }

        System.out.printf("The character '%c' appears %,d time(s) in the text.\n\n", target, count);
    }

    @Override
    public void handleWordFrequencyQuery(Scanner scanner) {
        System.out.print("Enter a word to search its frequency: ");
        String word = scanner.nextLine().trim();

        while (word.isEmpty()) {
            System.out.print("Word cannot be empty. Please enter a word: ");
            word = scanner.nextLine().trim();
        }

        String target = word.toLowerCase();
        String[] words = lowercaseText.split("\\s+");
        int count = 0;
        for (String w : words) {
            String cleaned = w.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            if (cleaned.equals(target)) {
                count++;
            }
        }

        System.out.printf("The word \"%s\" appears %,d time(s) in the text.\n\n", word, count);
    }

    @Override
    public void displayUniqueWordCount() {
        int uniqueCount = getUniqueWords().size();
        System.out.printf("Number of unique words (case-insensitive): %,d%n", uniqueCount);
    }
}