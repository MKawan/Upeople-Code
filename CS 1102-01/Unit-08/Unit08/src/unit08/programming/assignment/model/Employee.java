package unit08.programming.assignment.model;

/**
 *
 * @author mk
 */
/**
 * Employee.java
 * Represents an employee with name, age, department and salary.
 */
public class Employee {
    private final String name;
    private final int age;
    private final String department;
    private final double salary;

    public Employee(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    // ---- Getters -------------------------------------------------
    public String getName()         { return name; }
    public int    getAge()          { return age; }
    public String getDepartment()   { return department; }
    public double getSalary()       { return salary; }

    // ---- toString for debugging ----------------------------------
    @Override
    public String toString() {
        return String.format("Employee{name='%s', age=%d, dept='%s', salary=%.2f}",
                name, age, department, salary);
    }
}