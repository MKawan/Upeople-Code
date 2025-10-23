package org.upeople.assignment;

import org.upeople.assignment.handles.StudentPanel;
import org.upeople.assignment.manages.EnrollmentPanel;
import org.upeople.assignment.manages.GradePanel;
import org.upeople.assignment.model.Student;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

// Main application class for the Student Management System
public class StudentManagementSystem extends Application {
    private ObservableList<Student> students = FXCollections.observableArrayList();
    private Map<String, ObservableList<String>> enrollments = new HashMap<>();
    private Map<String, ObservableMap<String, String>> grades = new HashMap<>();
    
    @Override
    public void start(Stage primaryStage) {
        
        // Initialize sample data
        enrollments.put("CS101", FXCollections.observableArrayList());
        enrollments.put("MATH201", FXCollections.observableArrayList());
        
        // Create shared table
        TableView<Student> studentTable = new TableView<>();
        TableColumn<Student, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Student, String> coursesColumn = new TableColumn<>("Enrolled Courses");
        coursesColumn.setCellValueFactory(cellData -> {
            String studentId = cellData.getValue().getId();
            String courses = enrollments.entrySet().stream()
                .filter(entry -> entry.getValue().contains(studentId))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(courses.isEmpty() ? "None" : courses);
        });
        TableColumn<Student, String> gradesColumn = new TableColumn<>("Grades");
        gradesColumn.setCellValueFactory(cellData -> {
            String studentId = cellData.getValue().getId();
            if (grades.containsKey(studentId)) {
                String gradeStr = grades.get(studentId).entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
                return new javafx.beans.property.SimpleStringProperty(gradeStr.isEmpty() ? "None" : gradeStr);
            }
            return new javafx.beans.property.SimpleStringProperty("None");
        });
        studentTable.getColumns().addAll(idColumn, nameColumn, coursesColumn, gradesColumn);
        studentTable.setItems(students);
        
        // Set up main layout
        BorderPane root = new BorderPane();
        TabPane tabPane = new TabPane();
        
        // Create panels with shared table
        StudentPanel studentPanel = new StudentPanel(students, enrollments, grades, studentTable, null, null);
        EnrollmentPanel enrollmentPanel = new EnrollmentPanel(students, enrollments, studentTable);
        GradePanel gradePanel = new GradePanel(students, enrollments, grades, studentTable);
        // Update StudentPanel with references to other panels
        studentPanel = new StudentPanel(students, enrollments, grades, studentTable, enrollmentPanel, gradePanel);
        
        Tab studentTab = new Tab("Students", studentPanel);
        studentTab.setClosable(false);
        Tab enrollmentTab = new Tab("Enrollment", enrollmentPanel);
        enrollmentTab.setClosable(false);
        Tab gradeTab = new Tab("Grades", gradePanel);
        gradeTab.setClosable(false);
        
        VBox screamMaster = new VBox();
        //screamMaster.setSpacing(30);
        
        
        tabPane.getTabs().addAll(studentTab, enrollmentTab, gradeTab);
        screamMaster.getChildren().addAll(tabPane, studentTable);
        root.setCenter(screamMaster);
        
        // Set up scene and stage
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setTitle("Student Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}