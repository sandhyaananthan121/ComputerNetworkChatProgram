import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * This class represents the entry point of a secure chat program with file
 * transfer capability.
 * It initializes the server socket, prompts the user to enter the port number,
 * and starts reading and writing threads for secure communication.
 */
public class SecureChatProgramEnd {
    /**
     * The main method of the program.
     * It creates a server socket, obtains the port number, and starts reading and
     * writing threads.
     */
    public static void main(String[] args) {
        try {
            // Create a server socket
            System.out.println("Bob is Typing");
            System.out.println("Waiting for connection..."); // Notify that the server is waiting for connections
            ServerSocket serverSocket = new ServerSocket(0); // Create a server socket with an available port
            int port = serverSocket.getLocalPort(); // Get the local port number of the server
            System.out.println("Server started on port: " + port); // Display the port number the server is running on
            Scanner scanner = new Scanner(System.in); // Create a scanner object for user input
            System.out.print("Enter the port to connect: "); // Prompt the user to enter the port to connect
            port = scanner.nextInt(); // Read the port number entered by the user

            // Start reading thread
            Thread readingThread = new Thread(new ReadingThread(serverSocket)); // Create a thread for reading
            readingThread.start(); // Start the reading thread

            // Start writing thread
            Thread writingThread = new Thread(new WritingThread(port)); // Create a thread for writing
            writingThread.start(); // Start the writing thread
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
