package unit06.assignment;

/**
 *
 * @author mk
 */
/**
 * Concrete class representing a Magazine.
 * Uses String as ID type (e.g., "2025-12" for issue).
 */
class Magazine extends LibraryItem<String> {
    /**
     * Constructor for creating a Magazine instance.
     *
     * @param title     magazine title
     * @param publisher publisher name
     * @param issue     issue identifier (e.g., month-year)
     */
    public Magazine(String title, String publisher, String issue) {
        super(title, publisher, issue);
    }

    /**
     * Overrides toString to indicate this is a Magazine.
     */
    @Override
    public String toString() {
        return "[Magazine] " + super.toString();
    }
}