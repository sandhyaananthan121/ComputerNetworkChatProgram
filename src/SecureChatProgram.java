import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * This class represents a simple chat program with file transfer capability.
 * It allows users to communicate securely and exchange files.
 */
public class SecureChatProgram {
    private static volatile boolean shouldExit = false;

    /**
     * The main method of the program.
     * It initializes the server socket, prompts the user to enter the port number,
     * and starts reading and writing threads for communication.
     */
    public static void main(String[] args) {
        try {
            // Initialize the server socket
            String name = args[0];
            System.out.println(name + " is Typing\nWaiting for connection...");
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            System.out.println("Server started on port: " + port);
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter the port to connect: ");
            port = scanner.nextInt();

            // Start reading thread
            ReadingThread readingThread = new ReadingThread(serverSocket);
            readingThread.start();

            // Start writing thread
            WritingThread writingThread = new WritingThread(port);
            writingThread.start();

            // Wait for both threads to finish
            readingThread.join();
            writingThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


}

/**
 * This class represents the writing thread of the chat program.
 * It sends messages and files to the connected socket.
 */
class WritingThread extends Thread {
    private int port;

    /**
     * Constructor to initialize the port number.
     */
    public WritingThread(int port) {
        this.port = port;
    }

    /**
     * The run method of the writing thread.
     * It connects to the specified port, reads user input, and sends messages or
     * files.
     */
    public void run() {
        boolean connected = false;
        while (!connected) {
            try {
                // Connect to the specified port
                Socket socket = new Socket("localhost", port);
                System.out.println("Connected to port: " + port);
                connected = true;

                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                String input;

                while ((input = reader.readLine()) != null) {
                    writer.println(input);

                    // If the message is "transfer filename", initiate file transfer
                    if (input.startsWith("transfer ")) {
                        String filename = input.substring(9);
                        File_Transfer(filename, socket);
                    }
                    if (input.equalsIgnoreCase("quit")) {
                        System.exit(0);
                    }
                }

                socket.close();
            } catch (ConnectException e) {
                System.out.println("Wrong port. Please try again.");
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter the port to connect: ");
                port = scanner.nextInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to transfer a file through the socket.
     */
    private void File_Transfer(String filename, Socket socket) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            OutputStream outputStream = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            System.out.println("File sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/**
 * This class represents the reading thread of the chat program.
 * It receives messages and files from the connected socket.
 */
class ReadingThread extends Thread {
    private ServerSocket serverSocket;

    /**
     * Constructor to initialize the server socket.
     */
    public ReadingThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * The run method of the reading thread.
     * It accepts connections, reads messages or files, and handles them
     * accordingly.
     */
    public void run() {
        try {
            // Accept incoming connections
            Socket socket = serverSocket.accept();
            System.out.println("Connected to a client.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;

            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);

                // If the message is "transfer filename", receive the file
                if (message.startsWith("transfer ")) {
                    String filename = message.substring(9);
                    File_Received(filename, socket);
                }
            }

            socket.close();
        } catch (SocketException e) {
            // Connection reset by peer, handle gracefully
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to receive a file through the socket.
     */
    private void File_Received(String filename, Socket socket) {
        try {
            String newFilename = "new_" + filename;

            FileOutputStream fileOutputStream = new FileOutputStream(newFilename);
            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
