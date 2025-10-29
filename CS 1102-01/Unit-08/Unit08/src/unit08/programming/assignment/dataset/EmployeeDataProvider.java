package unit08.programming.assignment.dataset;

/**
 *
 * @author mk
 */
import java.util.ArrayList;
import java.util.List;
import unit08.programming.assignment.model.Employee;

/**
 * EmployeeDataProvider.java
 * Responsible for building the initial employee collection.
 * In a real project this would read from a file, DB, etc.
 */
public final class EmployeeDataProvider {

    private EmployeeDataProvider() {}   // static only

    /**
     * Returns a hard-coded list of employees.
     * Replace with I/O logic for production.
     */
    public static List<Employee> loadEmployees() {
        List<Employee> list = new ArrayList<>();

        list.add(new Employee("Alice Johnson", 34, "Engineering", 95_000));
        list.add(new Employee("Bob Smith",     28, "Marketing",   68_000));
        list.add(new Employee("Carol Lee",     41, "Engineering", 110_000));
        list.add(new Employee("David Brown",   29, "HR",         55_000));
        list.add(new Employee("Eve Davis",     37, "Marketing",   78_000));
        list.add(new Employee("Frank Wilson",  45, "Engineering", 120_000));
        list.add(new Employee("Grace Miller",  32, "HR",         60_000));

        return list;
    }
}