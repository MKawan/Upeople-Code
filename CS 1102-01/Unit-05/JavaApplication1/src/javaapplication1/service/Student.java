package javaapplication1.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mk
 */
// Represents a student with personal and academic information
public class Student {
    // Private instance variables for encapsulation
    private String name;
    private String id;
    private ArrayList<Course> enrolledCourses;
    private Map<Course, Double> grades;

    // Constructor to initialize student details
    public Student(String name, String id) {
        this.name = name;
        this.id = id;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    // Getter for student name
    public String getName() {
        return name;
    }

    // Getter for student ID
    public String getId() {
        return id;
    }

    // Getter for enrolled courses
    public ArrayList<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    // Setter for student name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for student ID
    public void setId(String id) {
        this.id = id;
    }

    // Enrolls student in a course if capacity allows
    public boolean enrollInCourse(Course course) {
        if (course.canEnroll()) {
            enrolledCourses.add(course);
            course.incrementEnrollment();
            return true;
        }
        return false;
    }

    // Assigns a grade for a course if student is enrolled and grade is valid
    public void assignGrade(Course course, double grade) {
        if (enrolledCourses.contains(course) && grade >= 0 && grade <= 100) {
            grades.put(course, grade);
        }
    }

    // Retrieves grade for a specific course
    public Double getGrade(Course course) {
        return grades.get(course);
    }
}