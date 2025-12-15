package org.unit05.disc;

/**
 *
 * @author mk
 */
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        // Primeiro: garante que o DB e a tabela existam
        DatabaseInitializer.initialize();

        EmployeeDAO dao = new EmployeeDAO();

        try {
            // Agora pode usar normalmente
            Employee newEmp = new Employee("Alice Johnson", "Software Engineer", 85000.00);
            dao.createEmployee(newEmp);

            System.out.println("\n--- all the Employees ---");
            for (Employee emp : dao.getAllEmployees()) {
                System.out.println(emp);
            }

            // Outras operações...

        } catch (SQLException e) {
            System.err.println("Erro no database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}