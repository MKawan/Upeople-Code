package javaapplication1.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mk
 */
// Manages collections of courses and students
public class CourseManagement {
    // Static variables to store all courses, students, and grades
    private static ArrayList<Course> courses = new ArrayList<>();
    private static ArrayList<Student> students = new ArrayList<>();
    private static Map<Student, Map<Course, Double>> studentGrades = new HashMap<>();

    // Adds a new course to the system
    public static void addCourse(String courseCode, String courseName, int maxCapacity) {
        Course course = new Course(courseCode, courseName, maxCapacity);
        courses.add(course);
    }

    // Adds a new student to the system
    public static void addStudent(String name, String id) {
        Student student = new Student(name, id);
        students.add(student);
        studentGrades.put(student, new HashMap<>());
    }

    // Enrolls a student in a course
    public static boolean enrollStudent(Student student, Course course) {
        if (student.enrollInCourse(course)) {
            studentGrades.get(student).put(course, null);
            return true;
        }
        return false;
    }

    // Assigns a grade to a student for a course
    public static void assignGrade(Student student, Course course, double grade) {
        if (studentGrades.containsKey(student) && studentGrades.get(student).containsKey(course)) {
            student.assignGrade(course, grade);
            studentGrades.get(student).put(course, grade);
        }
    }

    // Calculates the average grade for a student
    public static double calculateOverallGrade(Student student) {
        Map<Course, Double> grades = studentGrades.get(student);
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        int count = 0;
        for (Double grade : grades.values()) {
            if (grade != null) {
                sum += grade;
                count++;
            }
        }
        return count > 0 ? sum / count : 0.0;
    }

    // Getter for list of courses
    public static ArrayList<Course> getCourses() {
        return courses;
    }

    // Getter for list of students
    public static ArrayList<Student> getStudents() {
        return students;
    }
}