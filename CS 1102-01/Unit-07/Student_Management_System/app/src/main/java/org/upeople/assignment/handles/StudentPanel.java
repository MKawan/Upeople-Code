package org.upeople.assignment.handles;

import org.upeople.assignment.manages.EnrollmentPanel;
import org.upeople.assignment.manages.GradePanel;
import org.upeople.assignment.model.Student;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Map;

// Panel for managing student addition, update, and viewing
public class StudentPanel extends VBox {
    private ObservableList<Student> students;
    private Map<String, ObservableList<String>> enrollments;
    private Map<String, ObservableMap<String, String>> grades;
    private TableView<Student> studentTable;
    private EnrollmentPanel enrollmentPanel;
    private GradePanel gradePanel;
    
    // Constructor initializes the student panel with a shared table
    public StudentPanel(ObservableList<Student> students, Map<String, ObservableList<String>> enrollments, 
                       Map<String, ObservableMap<String, String>> grades, TableView<Student> studentTable,
                       EnrollmentPanel enrollmentPanel, GradePanel gradePanel) {
        this.students = students;
        this.enrollments = enrollments;
        this.grades = grades;
        this.studentTable = studentTable;
        this.enrollmentPanel = enrollmentPanel;
        this.gradePanel = gradePanel;
        setSpacing(10);
        setPadding(new Insets(10));
        
        // Form for adding/updating students
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");
        TextField idField = new TextField();
        idField.setPromptText("Student ID");
        
        Button addButton = new Button("Add Student");
        Button updateButton = new Button("Update Student");
        
        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("ID:"), 0, 1);
        form.add(idField, 1, 1);
        form.add(addButton, 0, 2);
        form.add(updateButton, 1, 2);
        
        // Set up table (shared instance, columns defined in StudentManagementSystem)
        for (ObservableList<String> studentList : enrollments.values()) {
            studentList.addListener((javafx.collections.ListChangeListener<String>) change -> {
                studentTable.refresh();
            });
        }
        for (ObservableMap<String, String> gradeMap : grades.values()) {
            gradeMap.addListener((javafx.collections.MapChangeListener<String, String>) change -> {
                studentTable.refresh();
            });
        }
        
        // Event handlers
        addButton.setOnAction(e -> addStudent(nameField, idField));
        updateButton.setOnAction(e -> updateStudent(nameField, idField));
        
        getChildren().addAll(new Label("Student Management"), form, studentTable);
    }
    
    // Adds a new student with validation
    private void addStudent(TextField nameField, TextField idField) {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        
        if (name.isEmpty() || id.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }
        
        if (students.stream().anyMatch(s -> s.getId().equals(id))) {
            showAlert("Error", "Student ID already exists");
            return;
        }
        
        students.add(new Student(id, name));
        enrollmentPanel.updateStudentComboBox();
        gradePanel.updateStudentComboBox();
        nameField.clear();
        idField.clear();
        showAlert("Success", "Student added successfully");
    }
    
    // Updates an existing student's information
    private void updateStudent(TextField nameField, TextField idField) {
        String name = nameField.getText().trim();
        String id = idField.getText().trim();
        
        if (name.isEmpty() || id.isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }
        
        for (Student student : students) {
            if (student.getId().equals(id)) {
                student.setName(name);
                enrollmentPanel.updateStudentComboBox();
                gradePanel.updateStudentComboBox();
                nameField.clear();
                idField.clear();
                showAlert("Success", "Student updated successfully");
                return;
            }
        }
        
        showAlert("Error", "Student not found");
    }
    
    // Shows alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}