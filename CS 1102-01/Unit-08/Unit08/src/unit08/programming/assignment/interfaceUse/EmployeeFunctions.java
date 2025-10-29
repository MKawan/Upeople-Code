package unit08.programming.assignment.interfaceUse;

/**
 *
 * @author mk
 */
import java.util.function.Function;
import unit08.programming.assignment.model.Employee;

/**
 * EmployeeFunctions.java
 * Contains reusable {@link Function} instances that transform an {@link Employee}.
 */
public final class EmployeeFunctions {

    private EmployeeFunctions() {}   // utility class

    /**
     * Function: Employee → "name (department)"
     * <p>
     * Purpose: Demonstrates the {@link Function} interface – a single abstract method
     * {@code apply(T)} that receives an input and returns an output.
     * </p>
     */
    public static final Function<Employee, String> NAME_DEPARTMENT_MAPPER =
            employee -> employee.getName() + " (" + employee.getDepartment() + ")";
}