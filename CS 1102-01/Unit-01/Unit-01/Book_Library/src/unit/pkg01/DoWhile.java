package unit.pkg01;
/**
 * @author mk
 */
import java.util.Scanner;

public class DoWhile {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        
        // Loop do-while: executa o bloco pelo menos uma vez
        do {
            System.out.println("\nMenu:");
            System.out.println("1. Option 1");
            System.out.println("2. Option 2");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            opcao = scanner.nextInt();
            
            switch (opcao) {
                case 1 -> System.out.println("You have selected the Option 1!");
                case 2 -> System.out.println("You have selected the Option 2!");
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("Option invalid!");
            }
        } while (opcao != 0); // Continua até o usuário escolher 0
        scanner.close();
    }
}
