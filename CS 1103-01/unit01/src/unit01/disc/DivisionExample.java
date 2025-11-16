package unit01.disc;

/**
 *
 * @author mk
 */
public class DivisionExample {

    public static void main(String[] args) {
        try {
            int numerator = 10;
            int denominator = 0;
            if (denominator == 0) {
                throw new ArithmeticException("Division by zero is not allowed!");
            }
            int result = numerator / denominator;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Cleanup complete, ready for next operation.");
        }
    }
}
