// Computer Networks CNT5106 Project 3
// By: Sandhya Ananthan UFID: 10953582 & Shreya Avadhuta UFID: 81198951

import java.io.*;  
import java.net.*;  // Importing necessary packages for file operations and networking
import java.util.Scanner;  // Importing Scanner class for user input

/**
 * This class represents a simple chat program with file transfer capability.
 * It allows users to communicate securely and exchange files.
 */
public class SecureChatProgram {
    private static volatile boolean shouldExit = false;  // Flag for controlling program termination

    /**
     * The main method of the program.
     * It initializes the server socket, prompts the user to enter the port number,
     * and starts reading and writing threads for communication.
     */
    public static void main(String[] args) {
        try {
            // Initialize the server socket
            String name = args[0];  // Extracting user name from command line arguments
            System.out.println(name + " is Typing\nWaiting for connection...");  // Displaying status
            ServerSocket serverSocket = new ServerSocket(0);  // Creating server socket with random port
            int port = serverSocket.getLocalPort();  // Getting the allocated port
            System.out.println("Server started on port: " + port);  // Displaying server port
            Scanner scanner = new Scanner(System.in);  // Creating scanner object for user input
            System.out.print("Enter the port to connect: ");  // Prompting user for port number
            port = scanner.nextInt();  // Reading user input for port

            // Start reading thread
            ReadingThread readingThread = new ReadingThread(serverSocket);  // Creating reading thread
            readingThread.start();  // Starting reading thread

            // Start writing thread
            WritingThread writingThread = new WritingThread(port);  // Creating writing thread
            writingThread.start();  // Starting writing thread

            // Wait for both threads to finish
            readingThread.join();  // Waiting for reading thread to finish
            writingThread.join();  // Waiting for writing thread to finish
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();  // Handling exceptions
        }
    }
}

/**
 * This class represents the writing thread of the chat program.
 * It sends messages and files to the connected socket.
 */
class WritingThread extends Thread {
    private int port;  // Port number to connect

    /**
     * Constructor to initialize the port number.
     */
    public WritingThread(int port) {
        this.port = port;  // Initializing port number
    }

    /**
     * The run method of the writing thread.
     * It connects to the specified port, reads user input, and sends messages or
     * files.
     */
    public void run() {
        boolean connected = false;  // Flag for connection status
        while (!connected) {
            try {
                // Connect to the specified port
                Socket socket = new Socket("localhost", port);  // Creating socket connection
                System.out.println("Connected to port: " + port);  // Displaying connection status
                connected = true;  // Updating connection status

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  // Creating reader object
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);  // Creating writer object

                String input;  // Variable to store user input

                while ((input = reader.readLine()) != null) {  // Reading user input
                    writer.println(input);  // Sending user input to server

                    // If the message is "transfer filename", initiate file transfer
                    if (input.startsWith("transfer ")) {  // Checking if file transfer command
                        String filename = input.substring(9);  // Extracting filename
                        File_Transfer(filename, socket);  // Initiating file transfer
                    }
                    if (input.equalsIgnoreCase("quit")) {  // Checking if quit command
                        System.exit(0);  // Exiting program
                    }
                }

                socket.close();  // Closing socket connection
            } catch (ConnectException e) {
                System.out.println("Wrong port. Please try again.");  // Handling connection error
                Scanner scanner = new Scanner(System.in);  // Creating scanner object
                System.out.print("Enter the port to connect: ");  // Prompting user for port number
                port = scanner.nextInt();  // Reading user input for port
            } catch (IOException e) {
                e.printStackTrace();  // Handling IO exceptions
            }
        }
    }

    /**
     * Method to transfer a file through the socket.
     */
    private void File_Transfer(String filename, Socket socket) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);  // Opening file input stream
            OutputStream outputStream = socket.getOutputStream();  // Creating output stream

            byte[] buffer = new byte[4096];  // Buffer for data transfer
            int bytesRead;  // Variable to store bytes read from file
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {  // Reading file data
                outputStream.write(buffer, 0, bytesRead);  // Writing data to output stream
            }

            fileInputStream.close();  // Closing file input stream
            System.out.println("File sent successfully");  // Displaying file transfer status
        } catch (Exception e) {
            e.printStackTrace();  // Handling exceptions
        }
    }
}

/**
 * This class represents the reading thread of the chat program.
 * It receives messages and files from the connected socket.
 */
class ReadingThread extends Thread {
    private ServerSocket serverSocket;  // Server socket for accepting connections

    /**
     * Constructor to initialize the server socket.
     */
    public ReadingThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;  // Initializing server socket
    }

    /**
     * The run method of the reading thread.
     * It accepts connections, reads messages or files, and handles them
     * accordingly.
     */
    public void run() {
        try {
            // Accept incoming connections
            Socket socket = serverSocket.accept();  // Accepting client connection
            System.out.println("Connected to a client.");  // Displaying connection status

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));  // Creating reader object

            String message;  // Variable to store received message

            while ((message = reader.readLine()) != null) {  // Reading message from client
                System.out.println("Received: " + message);  // Displaying received message

                // If the message is "transfer filename", receive the file
                if (message.startsWith("transfer ")) {  // Checking if file transfer command
                    String filename = message.substring(9);  // Extracting filename
                    File_Received(filename, socket);  // Initiating file receiving
                }
            }

            socket.close();  // Closing socket connection
        } catch (SocketException e) {
            // Connection reset by peer, handle gracefully
            System.exit(0);  // Exiting program
        } catch (IOException e) {
            e.printStackTrace();  // Handling IO exceptions
        }
    }

    /**
     * Method to receive a file through the socket.
     */
    private void File_Received(String filename, Socket socket) {
        try {
            String newFilename = "new_" + filename;  // Generating new filename for received file

            FileOutputStream fileOutputStream = new FileOutputStream(newFilename);  // Creating file output stream
            InputStream inputStream = socket.getInputStream();  // Creating input stream from socket

            byte[] buffer = new byte[4096];  // Buffer for data transfer
            int bytesRead;  // Variable to store bytes read from input stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {  // Reading data from input stream
                fileOutputStream.write(buffer, 0, bytesRead);  // Writing data to file output stream
            }

            fileOutputStream.close();  // Closing file output stream
        } catch (IOException e) {
            e.printStackTrace();  // Handling IO exceptions
        }
    }
}

