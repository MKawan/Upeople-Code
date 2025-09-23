package ClassStudent;
/**
 *
 * @author mk
 */
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

// Manages student records using static variables and methods in a classless structure
public class StudentRecordManagement {
    // Static variables to store student list and total count
    private static ArrayList<Student> students = new ArrayList<>();
    private static int totalStudents = 0;

    // Represents a student with individual attributes
    static class Student {
        String name;
        int id;
        int age;
        double grade;

        // Constructor for initializing student data
        Student(String name, int id, int age, double grade) {
            this.name = name;
            this.id = id;
            this.age = age;
            this.grade = grade;
        }

        // Returns a string representation of student details
        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Age: " + age + ", Grade: " + grade;
        }
    }

    // Adds a new student to the list
    private static void addStudent(String name, int id, int age, double grade) {
        // Check for duplicate ID
        for (Student student : students) {
            if (student.id == id) {
                System.out.println("Error: Student ID " + id + " already exists.");
                return;
            }
        }
        // Validate inputs
        if (name.isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return;
        }
        if (age < 0 || age > 120) {
            System.out.println("Error: Invalid age. Must be between 0 and 120.");
            return;
        }
        if (grade < 0.0 || grade > 100.0) {
            System.out.println("Error: Invalid grade. Must be between 0.0 and 100.0.");
            return;
        }
        // Add student and increment counter
        students.add(new Student(name, id, age, grade));
        totalStudents++;
        System.out.println("Student added successfully: " + name);
    }

    // Updates an existing student's information
    private static void updateStudent(int id, String name, int age, double grade) {
        // Find student by ID
        for (Student student : students) {
            if (student.id == id) {
                // Validate inputs before updating
                if (name.isEmpty()) {
                    System.out.println("Error: Name cannot be empty.");
                    return;
                }
                if (age < 0 || age > 120) {
                    System.out.println("Error: Invalid age. Must be between 0 and 120.");
                    return;
                }
                if (grade < 0.0 || grade > 100.0) {
                    System.out.println("Error: Invalid grade. Must be between 0.0 and 100.0.");
                    return;
                }
                // Update student details
                student.name = name;
                student.age = age;
                student.grade = grade;
                System.out.println("Student ID " + id + " updated successfully.");
                return;
            }
        }
        System.out.println("Error: Student ID " + id + " not found.");
    }

    // Displays details of a student by ID
    private static void viewStudent(int id) {
        for (Student student : students) {
            if (student.id == id) {
                System.out.println("Student Details: " + student);
                return;
            }
        }
        System.out.println("Error: Student ID " + id + " not found.");
    }

    // Displays all students in the system
    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students in the system.");
            return;
        }
        System.out.println("All Students (" + totalStudents + "):");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    // Displays the interactive menu and handles user input
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Display menu
            System.out.println("\n=== Student Record Management System ===");
            System.out.println("1. Add New Student");
            System.out.println("2. Update Student Information");
            System.out.println("3. View Student Details");
            System.out.println("4. View All Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1: // Add new student
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();
                        System.out.print("Enter grade (0.0-100.0): ");
                        double grade = scanner.nextDouble();
                        scanner.nextLine(); // Clear buffer
                        addStudent(name, id, age, grade);
                        break;

                    case 2: // Update student
                        System.out.print("Enter student ID to update: ");
                        id = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        System.out.print("Enter new name: ");
                        name = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        int newAge = scanner.nextInt();
                        System.out.print("Enter new grade (0.0-100.0): ");
                        double newGrade = scanner.nextDouble();
                        scanner.nextLine(); // Clear buffer
                        updateStudent(id, name, newAge, newGrade);
                        break;

                    case 3: // View student
                        System.out.print("Enter student ID to view: ");
                        id = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        viewStudent(id);
                        break;

                    case 4: // View all students
                        viewAllStudents();
                        break;

                    case 5: // Exit
                        System.out.println("Exiting system. Goodbye!");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Error: Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Invalid input. Please enter valid data.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
}