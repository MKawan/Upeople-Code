package unit01.disc;

/**
 *
 * @author mk
 */
import java.io.*;

public class FileExample {
    public static void main(String[] args) {
        FileReader file = null;
        try {
            file = new FileReader("data.txt");
            System.out.println("File accessed successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found - " + e.getMessage());
        } finally {
            try {
                if (file != null) {
                    file.close();
                    System.out.println("File closed.");
                }
            } catch (IOException e) {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
}
