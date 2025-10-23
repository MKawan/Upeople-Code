package org.upeople.assignment.handles;

import org.upeople.assignment.manages.EnrollmentPanel;
import org.upeople.assignment.manages.GradePanel;
import org.upeople.assignment.model.Student;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

// Panel for managing student addition, update, and viewing
public class StudentPanel extends VBox {
    private ObservableList<Student> students;
    private Map<String, ArrayList<String>> enrollments;
    private Map<String, Map<String, String>> grades;
    private TableView<Student> studentTable;
    private EnrollmentPanel enrollmentPanel;
    private GradePanel gradePanel;
    
    // Constructor initializes the student panel and references to other panels
    public StudentPanel(ObservableList<Student> students, Map<String, ArrayList<String>> enrollments, 
                       Map<String, Map<String, String>> grades, EnrollmentPanel enrollmentPanel, GradePanel gradePanel) {
        this.students = students;
        this.enrollments = enrollments;
        this.grades = grades;
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
        
        // Student table with dynamic columns
        studentTable = new TableView<>();
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
        studentTable.refresh(); // Refresh table to update courses and grades
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
                studentTable.refresh(); // Refresh table to update courses and grades
                nameField.clear();
                idField.clear();
                showAlert("Success", "Student updated successfully");
                return;
            }
        }
        
        showAlert("Error", "Student not found");
    }
    
    // Refreshes the table to reflect enrollment or grade changes
    public void refreshTable() {
        studentTable.refresh();
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