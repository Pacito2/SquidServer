package Main;

import Party.User;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server
{
    private static boolean clientSearchEnabled = true;
    private static final int CONNECTION_PORT = 5056;
    public static ArrayList<User> userList = new ArrayList<>();

    public static void main(String[] args)
    {
        System.out.println("Squid server launched");
        // Waiting for client connections
        while (clientSearchEnabled)
        {
            try(ServerSocket serverSocket = new ServerSocket(CONNECTION_PORT))
            {
                Socket clientSocket = serverSocket.accept();

                System.out.println("New client connected : " + clientSocket);

                DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

                // Adding new element to connected user list
                userList.add(new User("Pacito"+(int)(Math.random() * 10) + 1, clientSocket, reader, writer));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
} 