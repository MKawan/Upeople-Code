package unit06.assignment;

/**
 *
 * @author mk
 */

/**
 * Concrete class representing a Book.
 * Uses String as ID type (typically ISBN).
 */
class Book extends LibraryItem<String> {
    /**
     * Constructor for creating a Book instance.
     *
     * @param title  book title
     * @param author book author
     * @param isbn   International Standard Book Number
     */
    public Book(String title, String author, String isbn) {
        super(title, author, isbn);
    }

    /**
     * Overrides toString to indicate this is a Book.
     */
    @Override
    public String toString() {
        return "[Book] " + super.toString();
    }
}