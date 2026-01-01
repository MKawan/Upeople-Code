# Simple Java Chat Application

## Overview
This is a basic client-server chat application implemented in Java using socket programming. It allows multiple clients to connect to a central server, send messages, and receive broadcasts from other users. The server assigns unique IDs to each client. The user interface is text-based (console-driven).

## Requirements
- Java Development Kit (JDK) 8 or higher.
- Compile and run using `javac` and `java` commands.

## How to Run
1. **Compile the Classes**:
   - Compile the server: `javac ChatServer.java`
   - Compile the client: `javac ChatClient.java`

2. **Start the Server**:
   - Run: `java ChatServer`
   - The server will listen on port 12345.

3. **Start Clients** (in separate terminals):
   - Run: `java ChatClient`
   - Connects to localhost:12345.
   - Enter messages at the prompt; type 'exit' to quit.

4. **Testing**:
   - Open multiple client terminals to simulate users.
   - Messages sent by one client are broadcast to others, prefixed with the sender's User ID.

## Implementation Details
- **ChatServer**: Uses `ServerSocket` to accept connections. Each client is handled in a separate `ClientHandler` thread. Messages are broadcast using a synchronized method.
- **ChatClient**: Connects via `Socket`. Uses one thread for receiving messages and the main thread for sending user input.
- **Limitations**: No authentication, error recovery, or GUI. Assumes localhost; edit HOST in ChatClient for remote servers.
- **Error Handling**: Basic IOExceptions are caught and printed.

For questions, contact the developer.