package javaapplication1.service;

/**
 *
 * @author mk
 */
// Represents a university course with enrollment tracking
public class Course {
    // Private instance variables for course details
    private String courseCode;
    private String courseName;
    private int maxCapacity;
    // Static variable to track total enrollments across all courses
    private static int totalEnrolledStudents = 0;
    private int currentEnrollment;

    // Constructor to initialize course details
    public Course(String courseCode, String courseName, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.maxCapacity = maxCapacity;
        this.currentEnrollment = 0;
    }

    // Getter for course code
    public String getCourseCode() {
        return courseCode;
    }

    // Getter for course name
    public String getCourseName() {
        return courseName;
    }

    // Getter for maximum capacity
    public int getMaxCapacity() {
        return maxCapacity;
    }

    // Getter for current enrollment count
    public int getCurrentEnrollment() {
        return currentEnrollment;
    }

    // Setter for course code
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    // Setter for course name
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Setter for maximum capacity
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Checks if course has available capacity
    public boolean canEnroll() {
        return currentEnrollment < maxCapacity;
    }

    // Increments enrollment count for this course and total
    public void incrementEnrollment() {
        currentEnrollment++;
        totalEnrolledStudents++;
    }

    // Static method to retrieve total enrolled students across all courses
    public static int getTotalEnrolledStudents() {
        return totalEnrolledStudents;
    }
}
