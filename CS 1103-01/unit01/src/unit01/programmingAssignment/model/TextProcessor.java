package unit01.programmingAssignment.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
/**
 *
 * @author mk
 */
public abstract class TextProcessor {
    protected String originalText;
    protected String lowercaseText;

    public TextProcessor(String text) {
        this.originalText = text;
        this.lowercaseText = text.toLowerCase();
    }

    // Forma mais comum e recomendada em 99% dos projetos
    public abstract void displayBasicStats();
    public abstract void handleCharacterFrequencyQuery(Scanner scanner);
    public abstract void handleWordFrequencyQuery(Scanner scanner);
    public abstract void displayUniqueWordCount();

    // Common utility methods (protected so subclasses can reuse)
    protected int countCharacters() {
        return originalText.length();
    }

    protected int countWords() {
        if (originalText.trim().isEmpty()) return 0;
        String[] words = originalText.split("\\s+");
        return words.length;
    }

    protected char findMostCommonCharacter() {
        if (originalText.isEmpty()) return '?';

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : lowercaseText.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {  // ignore spaces and punctuation
                freq.put(c, freq.getOrDefault(c, 0) + 1);
            }
        }

        char mostCommon = '?';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostCommon = entry.getKey();
            }
        }
        return mostCommon == '?' ? lowercaseText.charAt(0) : mostCommon;
    }

    protected Set<String> getUniqueWords() {
        String[] words = lowercaseText.split("\\s+");
        Set<String> unique = new HashSet<>();
        for (String word : words) {
            String cleaned = word.replaceAll("[^a-zA-Z0-9]", ""); // remove punctuation
            if (!cleaned.isEmpty()) {
                unique.add(cleaned.toLowerCase());
            }
        }
        return unique;
    }
}
