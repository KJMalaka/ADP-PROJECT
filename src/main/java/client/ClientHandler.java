package client;

import domain.RequestDTO;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Client-side: No-argument constructor connects to server
    public ClientHandler() {
        try {
            this.socket = new Socket("localhost", 6666);
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Server-side: Accepts a socket from Server
    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Client-side: Send a request and get a response
    public Object sendRequest(RequestDTO request) {
        try {
            out.writeObject(request);
            out.flush();
            Object response = in.readObject();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Close the connection
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Server-side: Handle client requests
    @Override
    public void run() {
        try {
            while (true) {
                Object obj = in.readObject();
                if (obj instanceof RequestDTO) {
                    RequestDTO request = (RequestDTO) obj;
                    // TODO: Process the request and create a response
                    Object response = processRequest(request);
                    out.writeObject(response);
                    out.flush();
                } else {
                    // Unknown object received, break or ignore
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Client disconnected or error: " + e.getMessage());
        } finally {
            close();
        }
    }

    // Example request processing (replace with your logic)
    private Object processRequest(RequestDTO request) {
        // TODO: Implement your request handling logic here
        // For now, just echo the request back
        return request;
    }
}
