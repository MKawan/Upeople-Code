package unit06.assignment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mk
 */
/**
 * Generic catalog class that manages a collection of library items.
 * The type parameter is bounded: only subclasses of LibraryItem are allowed.
 * The wildcard <?> allows mixing different LibraryItem subtypes in one catalog.
 *
 * @param <T> must extend LibraryItem with any ID type
 */
class LibraryCatalog<T extends LibraryItem<?>> {
    private List<T> items;  // Internal list to store catalog items

    /**
     * Default constructor initializes an empty catalog.
     */
    public LibraryCatalog() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds a new item to the catalog.
     *
     * @param item the library item to add
     * @throws IllegalArgumentException if item is null
     */
    public void addItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the catalog.");
        }
        items.add(item);
        System.out.println("Item added successfully: " + item);
    }

    /**
     * Removes an item by its ID.
     *
     * @param id the ID of the item to remove (Object to support different ID types)
     * @throws IllegalArgumentException if no item with given ID exists
     */
    public void removeItem(Object id) {
        boolean removed = items.removeIf(item -> item.getItemID().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Item with ID " + id + " not found in the catalog.");
        }
        System.out.println("Item with ID " + id + " removed successfully.");
    }

    /**
     * Retrieves an item by its ID.
     *
     * @param id the ID to search for
     * @return the matching item, or null if not found
     */
    public T getItem(Object id) {
        for (T item : items) {
            if (item.getItemID().equals(id)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Displays all items currently in the catalog.
     * Shows a message if the catalog is empty.
     */
    public void displayCatalog() {
        if (items.isEmpty()) {
            System.out.println("The catalog is currently empty.");
        } else {
            System.out.println("\n=== Current Library Catalog ===");
            for (T item : items) {
                System.out.println(item);
            }
            System.out.println("Total items: " + items.size());
            System.out.println("===============================\n");
        }
    }

    /**
     * Returns a copy of the item list (for safe external access).
     *
     * @return a new list containing all items
     */
    public List<T> getAllItems() {
        return new ArrayList<>(items);
    }
}