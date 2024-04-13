import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatProgram {
    public static void main(String[] args) {
        try {
            // Create a server socket
            System.out.println("Alice is typing");
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            System.out.println("Port number: " + port);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the port you want to connect to: ");
            port = sc.nextInt();
            // Start reading thread
            ReadingThread readingThread = new ReadingThread(serverSocket);
            readingThread.start();

            // Start writing thread
            WritingThread writingThread = new WritingThread(port);
            writingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class WritingThread extends Thread {
    private int port;

    public WritingThread(int port) {
        this.port = port;
    }

    public void run() {
        try {
            // Connect to the specified port
            Socket socket = new Socket("localhost", port);
            System.out.println("Connected to port: " + port);

            BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String input;
            while ((input = keyboardReader.readLine()) != null) {
                out.println(input);

                // If the message is "transfer filename", transfer the file
                if (input.startsWith("transfer ")) {
                    String filename = input.substring(9);
                    transferFile(filename, socket);
                }
                if (input.equalsIgnoreCase("quit")) {
                    break;
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transferFile(String filename, Socket socket) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filename);
            OutputStream outputStream = socket.getOutputStream(); // Directly get the OutputStream from the socket

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

class ReadingThread extends Thread {
    private ServerSocket serverSocket;

    public ReadingThread(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run() {
        try {
            // Listen for incoming connections
            Socket socket = serverSocket.accept();
            System.out.println("Connected to a client.");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Received: " + message);

                // If the message is "transfer filename", receive the file
                if (message.startsWith("transfer ")) {
                    String filename = message.substring(9);
                    receiveFile(filename, socket);
                }
                if (message.equals("quit")) {
                    System.exit(0);
                }
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile(String filename, Socket socket) {
        try {
            // Change the name of the received file
            String newFilename = "new" + filename;

            FileOutputStream fileOutputStream = new FileOutputStream(newFilename);
            InputStream inputStream = socket.getInputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();

            // Send a confirmation message after file transfer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
