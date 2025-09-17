package br.com.mk.utils;

import br.com.mk.dtos.Book;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the library's book inventory and borrowed books using maps for efficient lookups by title.
 * Provides methods to add, borrow, and return books, handling quantity updates and validations.
 * Tracks borrowed books and supports displaying them.
 */
public class BookService {
    private Map<String, Book> library;
    private Map<String, Integer> borrowedBooks;

    /**
     * Constructor initializes an empty library map and borrowed books map, populates with 10 computer science books.
     */
    public BookService() {
        this.library = new HashMap<>();
        this.borrowedBooks = new HashMap<>();
        // Initialize with 10 computer science books
        String[][] initialBooks = {
            {"Introduction to Algorithms", "Thomas H. Cormen", "5"},
            {"Clean Code", "Robert C. Martin", "3"},
            {"Design Patterns", "Erich Gamma", "4"},
            {"The Art of Computer Programming", "Donald E. Knuth", "2"},
            {"Operating System Concepts", "Abraham Silberschatz", "6"},
            {"Computer Networks", "Andrew S. Tanenbaum", "3"},
            {"Database System Concepts", "Abraham Silberschatz", "4"},
            {"Structure and Interpretation of Computer Programs", "Harold Abelson", "3"},
            {"The Pragmatic Programmer", "Andrew Hunt", "5"},
            {"Code Complete", "Steve McConnell", "4"}
        };
        for (String[] bookInfo : initialBooks) {
            addBook(bookInfo[0], bookInfo[1], Integer.parseInt(bookInfo[2]));
        }
    }

    /**
     * Adds or updates a book in the library.
     * If the book exists, increments quantity; otherwise, adds a new book.
     * @param title The book's title.
     * @param author The book's author.
     * @param quantity The quantity to add.
     * @return true if added/updated successfully, false if invalid input.
     */
    public boolean addBook(String title, String author, int quantity) {
        if (title == null || title.trim().isEmpty() || author == null || author.trim().isEmpty() || quantity <= 0) {
            return false;
        }
        Book book = library.get(title.trim());
        if (book != null) {
            book.setQuantity(book.getQuantity() + quantity);
        } else {
            library.put(title.trim(), new Book(title.trim(), author.trim(), quantity));
        }
        return true;
    }

    /**
     * Borrows a specified number of books if available, updates borrowed books map.
     * Handles input with or without author name by extracting the title.
     * @param title The book's title, optionally including author (e.g., "Title by Author").
     * @param num The number to borrow.
     * @return true if borrowed successfully, false otherwise.
     */
    public boolean borrowBook(String title, int num) {
        if (num <= 0) return false;
        String cleanTitle = title.trim().split(" by ")[0].trim();
        Book book = library.get(cleanTitle);
        if (book != null && book.getQuantity() >= num) {
            book.setQuantity(book.getQuantity() - num);
            borrowedBooks.put(cleanTitle, borrowedBooks.getOrDefault(cleanTitle, 0) + num);
            return true;
        }
        return false;
    }

    /**
     * Returns a specified number of books to the library, updates borrowed books map.
     * Handles input with or without author name by extracting the title.
     * @param title The book's title, optionally including author (e.g., "Title by Author").
     * @param num The number to return.
     * @return true if returned successfully, false otherwise.
     */
    public boolean returnBook(String title, int num) {
        if (num <= 0) return false;
        String cleanTitle = title.trim().split(" by ")[0].trim();
        Book book = library.get(cleanTitle);
        Integer borrowedCount = borrowedBooks.get(cleanTitle);
        if (book != null && borrowedCount != null && borrowedCount >= num) {
            book.setQuantity(book.getQuantity() + num);
            borrowedBooks.put(cleanTitle, borrowedCount - num);
            if (borrowedBooks.get(cleanTitle) == 0) {
                borrowedBooks.remove(cleanTitle);
            }
            return true;
        }
        return false;
    }

    /**
     * Displays all books in the library.
     */
    public void displayLibrary() {
        if (library.isEmpty()) {
            System.out.println("Library is empty.");
            return;
        }
        for (Book book : library.values()) {
            System.out.println(book.getTitle() + " by " + book.getAuthor() + " - Quantity: " + book.getQuantity());
        }
    }

    /**
     * Displays all borrowed books.
     */
    public void displayBorrowedBooks() {
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books currently borrowed.");
            return;
        }
        for (Map.Entry<String, Integer> entry : borrowedBooks.entrySet()) {
            String title = entry.getKey();
            Book book = library.get(title);
            if (book != null) {
                System.out.println(book.getTitle() + " by " + book.getAuthor() + " - Borrowed: " + entry.getValue());
            }
        }
    }
}