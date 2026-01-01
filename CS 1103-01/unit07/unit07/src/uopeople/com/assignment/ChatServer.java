package uopeople.com.assignment;

/**
 *
 * @author mk
 */
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * ChatServer class: Manages multiple client connections for a simple chat application.
 * It listens for incoming connections, assigns unique IDs, and broadcasts messages to all connected clients.
 */
public class ChatServer {
    private static final int PORT = 12345; // Port number for the server
    private static List<ClientHandler> clients = new ArrayList<>(); // List to hold connected clients
    private static int userIdCounter = 1; // Counter for assigning unique user IDs

    public static void main(String[] args) {
        System.out.println("Chat Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept incoming client connection
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Create a new handler for the client and start it in a new thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, userIdCounter++);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     * @param message The message to broadcast.
     * @param sender The sender client handler (to exclude from broadcast).
     */
    public static synchronized void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    /**
     * Removes a client from the list when they disconnect.
     * @param client The client handler to remove.
     */
    public static synchronized void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("Client disconnected. Remaining clients: " + clients.size());
    }
}