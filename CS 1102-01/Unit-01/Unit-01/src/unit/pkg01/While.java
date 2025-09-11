package unit.pkg01;
/**
 * @author mk
 **/
import java.util.Scanner;
public class While {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String senha = "";
            
            // Loop while: continues while the password is less than 8 characters long
            while (senha.length() < 8) {
                System.out.println("Enter a password (minimum 8 characters): ");
                senha = scanner.nextLine();
                if (senha.length() < 8) {
                    System.out.println("Error: The password must be at least 8 characters long.");
                }
            }
            
            System.out.println("Valid password: " + senha);
            scanner.close();
        }
        
    }
}
