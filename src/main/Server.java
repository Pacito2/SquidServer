package main;

import party.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    /**
     * Server connection port
     */
    private static final int CONNECTION_PORT = 5056;
    /**
     * Allow connections from new clients
     */
    private static final boolean clientSearchEnabled = true;
    /**
     * List of connected users
     */
    public static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Squid server launched");
        // Waiting for client connections
        while (clientSearchEnabled) {
            try (ServerSocket serverSocket = new ServerSocket(CONNECTION_PORT)) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("New client connected : " + clientSocket);

                DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

                // Adding new element to connected user list
                userList.add(new User("P" + (int) (Math.random() * 100) + 1, clientSocket, reader, writer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
} 