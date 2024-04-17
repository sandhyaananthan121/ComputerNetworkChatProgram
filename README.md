Project Description: 
The chat program consists of two threads: a main thread, which also acts as the reading thread, and a writing thread. Upon starting, the main thread initiates the writing thread, which prompts the user to input a port number and establishes a connection to it. Subsequently, it enters a loop where it reads messages from the keyboard and sends them through the connection. If the message is "transfer filename", it transmits the specified file through the connection.

Additionally, the main thread creates a ServerSocket, prints out the port number used, and listens for incoming connections. Upon receiving a connection request, it establishes a connection socket and transitions into a reading thread. This thread listens for messages from the connection socket and prints them to the screen. If the message is "transfer filename", it reads the accompanying file and stores it locally. This process is reminiscent of the server code from a previous project.

Steps to run project:

1. Open two terminal windows.
2. Navigate to the directory containing your Java source files using the command 'cd src'.
3. In the first terminal, run 'java ChatProgram'.
4. In the second terminal, run 'java ChatProgramEnd'.
5. Enter the same port number when prompted in both terminals.
6. Begin by typing your messages in either terminal.
7. To transfer a file, type 'transfer filename' in the message input and press Enter.
8. To exit, type 'quit' in both terminals.
