package unit06.assignment;

/**
 *
 * @author mk
 */

/**
 * Abstract base class representing a generic library item.
 * All concrete library items (Book, DVD, Magazine, etc.) must extend this class.
 * The generic type <T> allows flexible ID types (e.g., String for ISBN, Integer for catalog number).
 *
 * @param <T> the type of the unique identifier for the item
 */

abstract class LibraryItem<T> {
    private String title;    // Title of the item (e.g., book name, movie title)
    private String author;   // Author, director, publisher, or creator
    private T itemID;        // Unique identifier (flexible type via generics)

    /**
     * Constructor to initialize a library item.
     *
     * @param title  the title of the item
     * @param author the author/creator of the item
     * @param itemID the unique ID of the item
     */
    public LibraryItem(String title, String author, T itemID) {
        this.title = title;
        this.author = author;
        this.itemID = itemID;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public T getItemID() {
        return itemID;
    }

    /**
     * Returns a string representation of the item.
     * Subclasses should override this to include their specific type.
     */
    @Override
    public String toString() {
        return "ID: " + itemID + " | Title: " + title + " | Author/Creator: " + author;
    }
}