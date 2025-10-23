package org.upeople.assignment.manages;

import org.upeople.assignment.model.Student;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Map;

// Panel for managing course enrollment
public class EnrollmentPanel extends VBox {
    private ObservableList<Student> students;
    private Map<String, ObservableList<String>> enrollments;
    private ComboBox<String> studentComboBox;
    private TextField courseField; // Declared as instance variable
    private TableView<Student> studentTable;
    
    // Constructor initializes the enrollment panel with a shared table
    public EnrollmentPanel(ObservableList<Student> students, Map<String, ObservableList<String>> enrollments, 
                          TableView<Student> studentTable) {
        this.students = students;
        this.enrollments = enrollments;
        this.studentTable = studentTable;
        setSpacing(10);
        setPadding(new Insets(10));
        
        // Form for enrolling students
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        studentComboBox = new ComboBox<>();
        updateStudentComboBox();
        courseField = new TextField(); // Initialize instance variable
        courseField.setPromptText("Course ID (e.g., CS101)");
        Button enrollButton = new Button("Enroll Student");
        
        form.add(new Label("Select Student:"), 0, 0);
        form.add(studentComboBox, 1, 0);
        form.add(new Label("Course ID:"), 0, 1);
        form.add(courseField, 1, 1);
        form.add(enrollButton, 1, 2);
        
        // Event handler for enrollment
        enrollButton.setOnAction(e -> enrollStudent(studentComboBox.getValue(), courseField.getText()));
        
        getChildren().addAll(new Label("Course Enrollment"), form, studentTable);
    }
    
    // Enrolls a student in a course
    private void enrollStudent(String studentId, String courseId) {
        if (students.isEmpty()) {
            showAlert("Error", "No students available to enroll");
            return;
        }
        
        if (studentId == null || courseId.trim().isEmpty()) {
            showAlert("Error", "Please select a student and enter a course ID");
            return;
        }
        
        if (!enrollments.containsKey(courseId)) {
            showAlert("Error", "Invalid course ID. Available courses: CS101, MATH201");
            return;
        }
        
        if (enrollments.get(courseId).contains(studentId)) {
            showAlert("Error", "Student already enrolled in this course");
            return;
        }
        
        enrollments.get(courseId).add(studentId); // Triggers table refresh via listener
        courseField.clear(); // Clear the course field after successful enrollment
        showAlert("Success", "Student enrolled successfully");
        studentTable.refresh();
    }
    
    // Updates the student ComboBox
    public void updateStudentComboBox() {
        studentComboBox.getItems().clear();
        for (Student student : students) {
            studentComboBox.getItems().add(student.getId());
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