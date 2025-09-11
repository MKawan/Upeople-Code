package unit.pkg01;

import java.util.Scanner;
/**
 * @author mk
 **/
public class For {
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter an integer for the Fibonacci sequence: ");
        int fat = scanner.nextInt();

        System.out.print("fibonacci of " + fat + " is ");
        
        for (int i = 0; i < fat; i++) {
            System.out.print(fibonacci(i) + " ");
        }
        
        System.out.print("\n");
        scanner.close();
    }
    // F(n + 2) = F(n + 1) + F(n), com F(0) = 0 e F(1) = 1, 0, 1, 1, 2, 3, 5, 8, 13, 21, 34...
    static int fibonacci(int num) {
        if (num < 2) {
            return 1;
        }
        return fibonacci(num - 1) + fibonacci(num - 2);
    }
}
