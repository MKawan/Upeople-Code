package unit06.assignment;

/**
 *
 * @author mk
 */

/**
 * Concrete class representing a DVD.
 * Uses Integer as ID type (e.g., internal catalog number).
 */
class DVD extends LibraryItem<Integer> {
    /**
     * Constructor for creating a DVD instance.
     *
     * @param title         movie or video title
     * @param director      director/creator
     * @param catalogNumber unique catalog number (integer)
     */
    public DVD(String title, String director, Integer catalogNumber) {
        super(title, director, catalogNumber);
    }

    /**
     * Overrides toString to indicate this is a DVD.
     */
    @Override
    public String toString() {
        return "[DVD] " + super.toString();
    }
}