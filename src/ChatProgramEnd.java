import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatProgramEnd {
    public static void main(String[] args) {
        try {
            // Create a server socket
            System.out.println("Bob is typing");
            ServerSocket serverSocket = new ServerSocket(0);
            int port = serverSocket.getLocalPort();
            System.out.println("Port number: " + port);
            Scanner sc = new Scanner(System.in);
            System.out.println("Enter the port you want to connect to: ");
            port= sc.nextInt();
            // Start reading thread
            Thread readingThread = new Thread(new ReadingThread(serverSocket));
            readingThread.start();

            // Start writing thread
            Thread writingThread = new Thread(new WritingThread(port));
            writingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

