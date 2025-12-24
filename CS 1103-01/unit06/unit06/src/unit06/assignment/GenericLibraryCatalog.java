package unit06.assignment;

import java.util.Scanner;

/**
 *
 * @author mk
 */
/**
 * Main class containing the command-line user interface and program entry point.
 * Demonstrates interaction with the generic catalog.
 */
public class GenericLibraryCatalog {
    public static void main(String[] args) {
        // Create a unified catalog that can hold any type of LibraryItem
        LibraryCatalog<LibraryItem<?>> catalog = new LibraryCatalog<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Welcome to the Generic Library Catalog System ===");
        System.out.println("This system uses Java generics for flexible item management.\n");

        while (true) {
            printMenu();
            int choice = readInt(scanner, "Choose an option (1-4): ");

            try {
                switch (choice) {
                    case 1:
                        addNewItem(catalog, scanner);
                        break;
                    case 2:
                        removeExistingItem(catalog, scanner);
                        break;
                    case 3:
                        catalog.displayCatalog();
                        break;
                    case 4:
                        System.out.println("\nThank you for using the Generic Library Catalog. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please select a number between 1 and 4.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    /**
     * Prints the main menu options.
     */
    private static void printMenu() {
        System.out.println("Main Menu:");
        System.out.println("1. Add New Item");
        System.out.println("2. Remove Item");
        System.out.println("3. View Catalog");
        System.out.println("4. Exit");
    }

    /**
     * Handles adding a new item by prompting user for type and details.
     */
    private static void addNewItem(LibraryCatalog<LibraryItem<?>> catalog, Scanner scanner) {
        System.out.println("\nSelect item type to add:");
        System.out.println("1. Book");
        System.out.println("2. DVD");
        System.out.println("3. Magazine");
        int type = readInt(scanner, "Choose type (1-3): ");

        System.out.print("Enter title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter author/creator/publisher: ");
        String creator = scanner.nextLine().trim();
        System.out.print("Enter ID (ISBN / Catalog Number / Issue): ");
        String idInput = scanner.nextLine().trim();

        LibraryItem<?> newItem = null;

        switch (type) {
            case 1: // Book
                newItem = new Book(title, creator, idInput);
                break;
            case 2: // DVD
                try {
                    int catalogNumber = Integer.parseInt(idInput);
                    newItem = new DVD(title, creator, catalogNumber);
                } catch (NumberFormatException e) {
                    System.out.println("Error: DVD catalog number must be a valid integer.\n");
                    return;
                }
                break;
            case 3: // Magazine
                newItem = new Magazine(title, creator, idInput);
                break;
            default:
                System.out.println("Invalid item type selected.\n");
                return;
        }

        catalog.addItem(newItem);
        System.out.println();
    }

    /**
     * Handles removing an item by ID.
     */
    private static void removeExistingItem(LibraryCatalog<LibraryItem<?>> catalog, Scanner scanner) {
        if (catalog.getAllItems().isEmpty()) {
            System.out.println("The catalog is empty. No items to remove.\n");
            return;
        }

        catalog.displayCatalog();
        System.out.print("Enter the ID of the item to remove: ");
        String idInput = scanner.nextLine().trim();

        // Try removing as String first, then as Integer if possible
        try {
            catalog.removeItem(idInput);
        } catch (IllegalArgumentException e1) {
            try {
                int intId = Integer.parseInt(idInput);
                catalog.removeItem(intId);
            } catch (NumberFormatException e2) {
                System.out.println("Error: No item found with the provided ID.\n");
            }
        }
        System.out.println();
    }

    /**
     * Utility method to safely read an integer from input with prompt.
     */
    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            scanner.next(); // clear invalid input
            System.out.print(prompt);
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return value;
    }
}