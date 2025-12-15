package org.unit05.disc;

/**
 *
 * @author mk
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private static final String INSERT_EMPLOYEE = 
        "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
    
    private static final String SELECT_EMPLOYEE_BY_ID = 
        "SELECT * FROM employees WHERE id = ?";
    
    private static final String SELECT_ALL_EMPLOYEES = 
        "SELECT * FROM employees";
    
    private static final String UPDATE_EMPLOYEE = 
        "UPDATE employees SET name = ?, position = ?, salary = ? WHERE id = ?";
    
    private static final String DELETE_EMPLOYEE = 
        "DELETE FROM employees WHERE id = ?";

    // CREATE
    public void createEmployee(Employee employee) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_EMPLOYEE)) {
            
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.executeUpdate();
            System.out.println("Employee created successfully.");
        }
    }

    // READ - Single employee by ID
    public Employee getEmployeeById(int id) throws SQLException {
        Employee employee = null;
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("position"),
                        rs.getDouble("salary")
                    );
                }
            }
        }
        return employee;
    }

    // READ - All employees
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_EMPLOYEES);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Employee emp = new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("position"),
                    rs.getDouble("salary")
                );
                employees.add(emp);
            }
        }
        return employees;
    }

    // UPDATE
    public void updateEmployee(Employee employee) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_EMPLOYEE)) {
            
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getPosition());
            pstmt.setDouble(3, employee.getSalary());
            pstmt.setInt(4, employee.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee updated successfully.");
            } else {
                System.out.println("No employee found with ID: " + employee.getId());
            }
        }
    }

    // DELETE
    public void deleteEmployee(int id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_EMPLOYEE)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Employee deleted successfully.");
            } else {
                System.out.println("No employee found with ID: " + id);
            }
        }
    }
}