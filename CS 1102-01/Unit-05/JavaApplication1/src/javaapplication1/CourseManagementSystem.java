package javaapplication1;

/**
 *
 * @author mk
 */
import java.util.ArrayList;
import java.util.Scanner;
import javaapplication1.service.Course;
import javaapplication1.service.CourseManagement;
import javaapplication1.service.Student;

// Main class with command-line interface for administrators
public class CourseManagementSystem {
    // Scanner for user input
    private static Scanner scanner = new Scanner(System.in);

    // Main method to run the interactive system
    public static void main(String[] args) {
        while (true) {
            displayMenu();
            int choice = getValidChoice();
            if (choice == 7) {
                System.out.println("Exiting system...");
                break;
            }
            handleMenuChoice(choice);
        }
        scanner.close();
    }

    // Displays the menu options
    private static void displayMenu() {
        System.out.println("\nCourse Enrollment and Grade Management System");
        System.out.println("1. Add new course");
        System.out.println("2. Add new student");
        System.out.println("3. Enroll student in course");
        System.out.println("4. Assign grade");
        System.out.println("5. Calculate overall grade");
        System.out.println("6. Update course information");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    // Validates user menu choice
    private static int getValidChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice < 1 || choice > 7) {
                System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                return getValidChoice();
            }
            return choice;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getValidChoice();
        }
    }

    // Handles menu selection
    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1:
                addNewCourse();
                break;
            case 2:
                addNewStudent();
                break;
            case 3:
                enrollStudent();
                break;
            case 4:
                assignGrade();
                break;
            case 5:
                calculateOverallGrade();
                break;
            case 6:
                updateCourse();
                break;
        }
    }

    // Adds a new course with user input
    private static void addNewCourse() {
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter maximum capacity: ");
        try {
            int capacity = Integer.parseInt(scanner.nextLine());
            if (capacity <= 0) {
                System.out.println("Capacity must be positive.");
                return;
            }
            CourseManagement.addCourse(code, name, capacity);
            System.out.println("Course added successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid capacity. Please enter a number.");
        }
    }

    // Adds a new student with user input
    private static void addNewStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        CourseManagement.addStudent(name, id);
        System.out.println("Student added successfully.");
    }

    // Enrolls a student in a course
    private static void enrollStudent() {
        Student student = selectStudent();
        if (student == null) return;
        Course course = selectCourse();
        if (course == null) return;
        if (CourseManagement.enrollStudent(student, course)) {
            System.out.println("Student enrolled successfully.");
        } else {
            System.out.println("Enrollment failed. Course may be full or student already enrolled.");
        }
    }

    // Assigns a grade to a student for a course
    private static void assignGrade() {
        Student student = selectStudent();
        if (student == null) return;
        Course course = selectCourse();
        if (course == null) return;
        System.out.print("Enter grade (0-100): ");
        try {
            double grade = Double.parseDouble(scanner.nextLine());
            if (grade < 0 || grade > 100) {
                System.out.println("Grade must be between 0 and 100.");
                return;
            }
            CourseManagement.assignGrade(student, course, grade);
            System.out.println("Grade assigned successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid grade. Please enter a number.");
        }
    }

    // Calculates and displays a student's overall grade
    private static void calculateOverallGrade() {
        Student student = selectStudent();
        if (student == null) return;
        double overallGrade = CourseManagement.calculateOverallGrade(student);
        System.out.printf("Overall grade for %s: %.2f%n", student.getName(), overallGrade);
    }

    // Updates course information based on user input
    private static void updateCourse() {
        Course course = selectCourse();
        if (course == null) return;
        System.out.print("Enter new course code (leave blank to keep current): ");
        String code = scanner.nextLine();
        if (!code.isEmpty()) course.setCourseCode(code);
        System.out.print("Enter new course name (leave blank to keep current): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) course.setCourseName(name);
        System.out.print("Enter new maximum capacity (leave blank to keep current): ");
        String capacityInput = scanner.nextLine();
        if (!capacityInput.isEmpty()) {
            try {
                int capacity = Integer.parseInt(capacityInput);
                if (capacity <= 0) {
                    System.out.println("Capacity must be positive.");
                    return;
                }
                course.setMaxCapacity(capacity);
            } catch (NumberFormatException e) {
                System.out.println("Invalid capacity. Please enter a number.");
                return;
            }
        }
        System.out.println("Course updated successfully.");
    }

    // Displays and selects a student from the list
    private static Student selectStudent() {
        ArrayList<Student> students = CourseManagement.getStudents();
        if (students.isEmpty()) {
            System.out.println("No students available.");
            return null;
        }
        System.out.println("Available students:");
        for (int i = 0; i < students.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, students.get(i).getName(), students.get(i).getId());
        }
        System.out.print("Select student number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < students.size()) {
                return students.get(index);
            }
            System.out.println("Invalid selection.");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }
    }

    // Displays and selects a course from the list
    private static Course selectCourse() {
        ArrayList<Course> courses = CourseManagement.getCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
            return null;
        }
        System.out.println("Available courses:");
        for (int i = 0; i < courses.size(); i++) {
            System.out.printf("%d. %s (%s)%n", i + 1, courses.get(i).getCourseName(), courses.get(i).getCourseCode());
        }
        System.out.print("Select course number: ");
        try {
            int index = Integer.parseInt(scanner.nextLine()) - 1;
            if (index >= 0 && index < courses.size()) {
                return courses.get(index);
            }
            System.out.println("Invalid selection.");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return null;
        }
    }
}