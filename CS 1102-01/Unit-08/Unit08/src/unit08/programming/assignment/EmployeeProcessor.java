package unit08.programming.assignment;

/**
 *
 * @author mk
 */
import java.util.*;
import java.util.stream.Collectors;
import static java.lang.System.out;

import unit08.programming.assignment.dataset.EmployeeDataProvider;
import unit08.programming.assignment.interfaceUse.EmployeeFunctions;
import unit08.programming.assignment.model.Employee;

/**
 * EmployeeProcessor.java
 * Demonstrates:
 *   • Function interface usage
 *   • Stream pipelines (map, filter, collect, averaging)
 *   • Lazy evaluation & short-circuiting
 *   • Parallel streams for large data
 *   • Bonus: department statistics, sorted output, configurable threshold
 */
public class EmployeeProcessor {

    private static final int DEFAULT_AGE_THRESHOLD = 30;

    public static void main(String[] args) {
        // 1. Load dataset
        List<Employee> employees = EmployeeDataProvider.loadEmployees();
        out.println("=== Dataset loaded: " + employees.size() + " employees ===\n");

        // 2. Transform to concatenated strings using Function
        List<String> nameDeptList = employees.stream()
                .map(EmployeeFunctions.NAME_DEPARTMENT_MAPPER)
                .collect(Collectors.toList());

        out.println("=== Name (Department) List ===");
        nameDeptList.forEach(out::println);
        out.println();

        // 3. Average salary (all employees)
        double avgSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
        out.printf("Average salary (all): $%,.2f%n%n", avgSalary);

        // 4. Filter by age threshold + reuse mapper
        int threshold = DEFAULT_AGE_THRESHOLD;
        List<String> seniors = employees.stream()
                .filter(e -> e.getAge() > threshold)
                .map(EmployeeFunctions.NAME_DEPARTMENT_MAPPER)
                .collect(Collectors.toList());

        out.println("=== Employees older than " + threshold + " ===");
        seniors.forEach(out::println);
        out.println();

        // 5. Average salary of filtered employees
        double seniorAvg = employees.stream()
                .filter(e -> e.getAge() > threshold)
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
        out.printf("Average salary (> %d yrs): $%,.2f%n%n", threshold, seniorAvg);

        // ---------------------- BONUS FEATURES ----------------------

        // Bonus 1: Department summary with statistics
        out.println("=== Department Summary ===");
        Map<String, DoubleSummaryStatistics> deptStats = employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.summarizingDouble(Employee::getSalary)
                ));

        deptStats.forEach((dept, stats) ->
                out.printf("%-12s | Count: %2d | Avg: $%8.2f | Min: $%8.2f | Max: $%8.2f%n",
                        dept, stats.getCount(), stats.getAverage(),
                        stats.getMin(), stats.getMax()));
        out.println();

        // Bonus 2: Sorted list of senior employees by salary (descending)
        out.println("=== Senior Employees (Salary DESC) ===");
        employees.stream()
                .filter(e -> e.getAge() > threshold)
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .forEach(e -> out.printf("%-20s | Age: %2d | Salary: $%8.2f%n",
                        EmployeeFunctions.NAME_DEPARTMENT_MAPPER.apply(e),
                        e.getAge(), e.getSalary()));
        out.println();

        // Bonus 3: Parallel stream performance demo (simulated large dataset)
        demoParallelProcessing();
    }

    /** Simulates processing 100,000 employees to show parallel stream benefit */
    private static void demoParallelProcessing() {
        out.println("=== Parallel Stream Demo (100K employees) ===");
        List<Employee> large = generateLargeDataset(100_000);

        long startSeq = System.nanoTime();
        double seqAvg = large.stream()
                .filter(e -> e.getAge() > 30)
                .mapToDouble(Employee::getSalary)
                .average().orElse(0.0);
        long endSeq = System.nanoTime();

        long startPar = System.nanoTime();
        double parAvg = large.parallelStream()
                .filter(e -> e.getAge() > 30)
                .mapToDouble(Employee::getSalary)
                .average().orElse(0.0);
        long endPar = System.nanoTime();

        out.printf("Sequential avg: $%.2f (%.2f ms)%n", seqAvg, (endSeq - startSeq) / 1_000_000.0);
        out.printf("Parallel   avg: $%.2f (%.2f ms)%n", parAvg, (endPar - startPar) / 1_000_000.0);
    }

    /** Helper to generate synthetic large dataset */
    private static List<Employee> generateLargeDataset(int size) {
        Random rnd = new Random(42);
        List<Employee> list = new ArrayList<>(size);
        String[] names = {"Alice", "Bob", "Carol", "David", "Eve", "Frank", "Grace", "Hank"};
        String[] depts = {"Engineering", "Marketing", "HR", "Sales"};

        for (int i = 0; i < size; i++) {
            String name = names[rnd.nextInt(names.length)] + " " + (i % 100);
            int age = 22 + rnd.nextInt(40);
            String dept = depts[rnd.nextInt(depts.length)];
            double salary = 50_000 + rnd.nextDouble() * 100_000;
            list.add(new Employee(name, age, dept, salary));
        }
        return list;
    }
}