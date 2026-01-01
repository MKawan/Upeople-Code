package uopeople.com.assignment;

/**
 *
 * @author mk
 */
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * ChatClient class: Connects to the ChatServer and allows users to send/receive messages.
 * It runs two threads: one for sending messages and one for receiving them.
 */
public class ChatClient {
    private static final String HOST = "localhost"; // Server hostname or IP
    private static final int PORT = 12345; // Server port

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server at " + HOST + ":" + PORT);

            // Start a thread to listen for incoming messages
            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        System.out.println(message); // Display received message
                    }
                } catch (IOException e) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
            }).start();

            // Main thread for sending messages
            Scanner scanner = new Scanner(System.in);
            String userInput;
            while (true) {
                System.out.print("Enter message (or 'exit' to quit): ");
                userInput = scanner.nextLine();
                if ("exit".equalsIgnoreCase(userInput)) {
                    break;
                }
                out.println(userInput); // Send message to server
            }
        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
        System.out.println("Disconnected from server.");
    }
}