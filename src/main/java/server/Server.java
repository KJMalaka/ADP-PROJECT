package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import client.ClientHandler;

public class Server {
    public static void main(String[] args) {
        int port = 6666;

        System.out.println("STUDENT ENROLMENT SYSTEM - SERVER");
        System.out.println("Starting server on port " + port);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started, waiting for clients...");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Client connected: " + clientSocket.getRemoteSocketAddress());

                    // If ClientHandler extends Thread:
                    // new ClientHandler(clientSocket).start();

                    // If ClientHandler implements Runnable:
                    new Thread(new ClientHandler(clientSocket)).start();

                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
