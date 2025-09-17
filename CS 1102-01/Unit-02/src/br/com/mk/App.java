package br.com.mk;

import br.com.mk.utils.BookService;
import java.util.Scanner;

/**
 * Main entry point for the library management system.
 * Handles user input via a menu-driven interface with a switch statement for options.
 * Uses Scanner for input and delegates operations to BookService.
 */
public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BookService service = new BookService();
        int choice;

        System.out.println("Welcome to the Library Management System!");

        do {
            System.out.println("\nOptions:");
            System.out.println("1. Add Books");
            System.out.println("2. Borrow Books");
            System.out.println("3. Return Books");
            System.out.println("4. Display Library");
            System.out.println("5. My Books");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                choice = 0;
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine().trim();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine().trim();
                    System.out.print("Enter quantity: ");
                    try {
                        int qty = Integer.parseInt(scanner.nextLine().trim());
                        if (service.addBook(title, author, qty)) {
                            System.out.println("Book added/updated successfully.");
                        } else {
                            System.out.println("Invalid input for adding book.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid quantity. Must be a positive number.");
                    }
                    break;

                case 2:
                    System.out.print("Enter book title (e.g., Computer Networks): ");
                    title = scanner.nextLine().trim();
                    System.out.print("Enter number to borrow: ");
                    try {
                        int num = Integer.parseInt(scanner.nextLine().trim());
                        if (service.borrowBook(title, num)) {
                            System.out.println("Books borrowed successfully.");
                        } else {
                            System.out.println("Cannot borrow: Insufficient quantity or book not found.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Must be a positive integer.");
                    }
                    break;

                case 3:
                    System.out.print("Enter book title (e.g., Computer Networks): ");
                    title = scanner.nextLine().trim();
                    System.out.print("Enter number to return: ");
                    try {
                        int num = Integer.parseInt(scanner.nextLine().trim());
                        if (service.returnBook(title, num)) {
                            System.out.println("Books returned successfully.");
                        } else {
                            System.out.println("Cannot return: Book not found, insufficient borrowed quantity, or invalid number.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid number. Must be a positive integer.");
                    }
                    break;

                case 4:
                    System.out.println("");
                    service.displayLibrary();
                    break;

                case 5:
                    System.out.println("\nMy Borrowed Books:");
                    service.displayBorrowedBooks();
                    System.out.println("\nOptions:");
                    System.out.println("1. Return a Book");
                    System.out.println("2. Back to Main Menu");
                    System.out.print("Enter your choice: ");
                    try {
                        int subChoice = Integer.parseInt(scanner.nextLine().trim());
                        if (subChoice == 1) {
                            System.out.print("Enter book title to return (e.g., Computer Networks): ");
                            title = scanner.nextLine().trim();
                            System.out.print("Enter number to return: ");
                            try {
                                int num = Integer.parseInt(scanner.nextLine().trim());
                                if (service.returnBook(title, num)) {
                                    System.out.println("Books returned successfully.");
                                } else {
                                    System.out.println("Cannot return: Book not found, insufficient borrowed quantity, or invalid number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid number. Must be a positive integer.");
                            }
                        } else if (subChoice != 2) {
                            System.out.println("Invalid choice. Please select 1 or 2.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a number.");
                    }
                    break;

                case 6:
                    System.out.println("Thank you for using the Library Management System!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1-6.");
            }
        } while (choice != 6);

        scanner.close();
    }
}