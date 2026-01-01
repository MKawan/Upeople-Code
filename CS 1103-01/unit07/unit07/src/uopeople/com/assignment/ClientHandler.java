package uopeople.com.assignment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author mk
 */

/**
 * ClientHandler class: Inner class to handle individual client connections.
 * Runs in a separate thread to manage reading and writing messages.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private int userId;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket, int userId) {
        this.socket = socket;
        this.userId = userId;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Send welcome message with user ID
            out.println("Welcome! Your User ID is " + userId);
        } catch (IOException e) {
            System.err.println("Error initializing client handler: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                String formattedMessage = "User " + userId + ": " + message;
                System.out.println("Received: " + formattedMessage);
                ChatServer.broadcast(formattedMessage, this); // Broadcast to others
            }
        } catch (IOException e) {
            System.err.println("Client read error: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Error closing socket: " + e.getMessage());
            }
            ChatServer.removeClient(this);
        }
    }

    /**
     * Sends a message to this client.
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
    }
}