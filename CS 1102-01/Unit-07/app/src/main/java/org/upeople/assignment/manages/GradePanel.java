package org.upeople.assignment.manages;

import org.upeople.assignment.handles.StudentPanel;
import org.upeople.assignment.model.Student;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// Panel for managing grade assignment
public class GradePanel extends VBox {
    private ObservableList<Student> students;
    private Map<String, ArrayList<String>> enrollments;
    private Map<String, Map<String, String>> grades;
    private ComboBox<String> studentComboBox;
    private ComboBox<String> courseComboBox;
    private TextField gradeField; // Declared as instance variable
    private StudentPanel studentPanel;
    
    // Constructor initializes the grade panel
    public GradePanel(ObservableList<Student> students, Map<String, ArrayList<String>> enrollments, 
                     Map<String, Map<String, String>> grades, StudentPanel studentPanel) {
        this.students = students;
        this.enrollments = enrollments;
        this.grades = grades;
        this.studentPanel = studentPanel;
        setSpacing(10);
        setPadding(new Insets(10));
        
        // Form for assigning grades
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        studentComboBox = new ComboBox<>();
        updateStudentComboBox();
        courseComboBox = new ComboBox<>();
        gradeField = new TextField(); // Initialize instance variable
        gradeField.setPromptText("Grade (e.g., A, B)");
        Button assignButton = new Button("Assign Grade");
        
        form.add(new Label("Select Student:"), 0, 0);
        form.add(studentComboBox, 1, 0);
        form.add(new Label("Select Course:"), 0, 1);
        form.add(courseComboBox, 1, 1);
        form.add(new Label("Grade:"), 0, 2);
        form.add(gradeField, 1, 2);
        form.add(assignButton, 1, 3);
        
        // Event handlers
        studentComboBox.setOnAction(e -> updateCourseComboBox());
        assignButton.setOnAction(e -> assignGrade(studentComboBox.getValue(), courseComboBox.getValue(), gradeField.getText()));
        
        getChildren().addAll(new Label("Grade Management"), form);
    }
    
    // Assigns a grade to a student for a course
    private void assignGrade(String studentId, String courseId, String grade) {
        if (students.isEmpty()) {
            showAlert("Error", "No students available to assign grades");
            return;
        }
        
        if (studentId == null || courseId == null || grade.trim().isEmpty()) {
            showAlert("Error", "Please select a student, course, and enter a grade");
            return;
        }
        
        if (!grades.containsKey(studentId)) {
            grades.put(studentId, new HashMap<>());
        }
        
        grades.get(studentId).put(courseId, grade);
        studentPanel.refreshTable(); // Notify StudentPanel to update table
        gradeField.clear(); // Clear the grade field after assignment
        showAlert("Success", "Grade assigned successfully");
    }
    
    // Updates the student ComboBox
    public void updateStudentComboBox() {
        studentComboBox.getItems().clear();
        for (Student student : students) {
            studentComboBox.getItems().add(student.getId());
        }
    }
    
    // Updates the course ComboBox based on selected student
    private void updateCourseComboBox() {
        courseComboBox.getItems().clear();
        String studentId = studentComboBox.getValue();
        if (studentId != null) {
            for (String courseId : enrollments.keySet()) {
                if (enrollments.get(courseId).contains(studentId)) {
                    courseComboBox.getItems().add(courseId);
                }
            }
            if (courseComboBox.getItems().isEmpty()) {
                showAlert("Info", "No courses enrolled for this student");
            }
        }
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