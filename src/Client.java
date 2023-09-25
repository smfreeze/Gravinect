package src;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        final String serverHost = "82.165.200.74"; // Replace with the server's IP address or hostname
        final int serverPort = 42069; // Replace with the server's port number

        try (Socket socket = new Socket(serverHost, serverPort);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Send a message to the server
            out.println("Hello, Server!");

            // Read and print the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
