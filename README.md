Project Description: 
The chat program consists of two threads: a main thread, which also acts as the reading thread, and a writing thread. Upon starting, the main thread initiates the writing thread, which prompts the user to input a port number and establishes a connection to it. Subsequently, it enters a loop where it reads messages from the keyboard and sends them through the connection. If the message is "transfer filename", it transmits the specified file through the connection.

Additionally, the main thread creates a ServerSocket, prints out the port number used, and listens for incoming connections. Upon receiving a connection request, it establishes a connection socket and transitions into a reading thread. This thread listens for messages from the connection socket and prints them to the screen. If the message is "transfer filename", it reads the accompanying file and stores it locally. This process is reminiscent of the server code from a previous project.

Steps to run project:

1. Open two terminal windows.
2. Navigate to the directory containing your Java source files using the command 'cd src'.
3. In the first terminal, run 'java SecureChatProgram'.
4. In the second terminal, run 'java SecureChatProgramEnd'.
5. Enter the port number displayed in the respective terminal when prompted in both terminals.
6. To establish a connection between Alice and Bob:
   -In Alice's terminal, type the port number displayed in Bob's terminal and press Enter.
   -In Bob's terminal, type the port number displayed in Alice's terminal and press Enter.
7. Start typing your messages in either terminal. Messages typed in one terminal should appear in the other.
To transfer a file from Alice to Bob:
8. In Alice's terminal, type 'transfer filename' (replace 'filename' with the actual name of the file) and press Enter.
9. To exit the program, type 'quit' in both terminals and press Enter.
