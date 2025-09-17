package br.com.mk.dtos;

/**
 * Represents a book in the library with title, author, and available quantity.
 * This class encapsulates the basic attributes of a book and provides methods to access and modify them.
 */

public class Book {
    private String title;
    private String author;
    private int quantity;

    /**
     * Constructor to initialize a book with title, author, and quantity.
     * @param title The title of the book.
     * @param author The author of the book.
     * @param quantity The initial quantity available.
     */
    public Book(String title, String author, int quantity) {
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}