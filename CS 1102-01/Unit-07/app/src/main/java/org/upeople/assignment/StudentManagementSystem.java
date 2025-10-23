package org.upeople.assignment;

import org.upeople.assignment.handles.StudentPanel;
import org.upeople.assignment.manages.EnrollmentPanel;
import org.upeople.assignment.manages.GradePanel;
import org.upeople.assignment.model.Student;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Main application class for the Student Management System
public class StudentManagementSystem extends Application {
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private Map<String, ArrayList<String>> enrollments = new HashMap<>();
    private Map<String, Map<String, String>> grades = new HashMap<>();
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize sample data
        initializeSampleData();
        
        // Set up main layout
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        
        // Create panels
        StudentPanel studentPanel = new StudentPanel(students, enrollments, grades, null, null);
        EnrollmentPanel enrollmentPanel = new EnrollmentPanel(students, enrollments, studentPanel);
        GradePanel gradePanel = new GradePanel(students, enrollments, grades, studentPanel);
        // Update StudentPanel with references to other panels
        studentPanel = new StudentPanel(students, enrollments, grades, enrollmentPanel, gradePanel);
        
        Tab studentTab = new Tab("Students", studentPanel);
        studentTab.setClosable(false);
        Tab enrollmentTab = new Tab("Enrollment", enrollmentPanel);
        enrollmentTab.setClosable(false);
        Tab gradeTab = new Tab("Grades", gradePanel);
        gradeTab.setClosable(false);
        
        tabPane.getTabs().addAll(studentTab, enrollmentTab, gradeTab);
        root.setCenter(tabPane);
        
        // Set up scene and stage
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    // Initializes sample data for testing
    private void initializeSampleData() {
        enrollments.put("CS101", new ArrayList<>());
        enrollments.put("MATH201", new ArrayList<>());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}