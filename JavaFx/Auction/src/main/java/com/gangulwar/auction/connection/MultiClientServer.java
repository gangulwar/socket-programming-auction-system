package com.gangulwar.auction.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MultiClientServer {

    private static final int PORT = 12345;
    private static Map<Socket, User> connectedClients = new HashMap<>();

    public static void StartServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            writer.println("Enter your username:");
            String username = reader.readLine();
            connectedClients.put(clientSocket, new User(username,"0",0));

            broadcast("User '" + username + "' has joined.");

            new Thread(() -> handleClientInteraction(clientSocket, reader, writer)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientInteraction(Socket clientSocket, BufferedReader reader, PrintWriter writer) {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                connectedClients.get(clientSocket).currentBid=message;
                broadcast(connectedClients.get(clientSocket).username + ": " + message);
            }
        } catch (IOException e) {
            String disconnectedUsername = connectedClients.get(clientSocket).username;
            connectedClients.remove(clientSocket);
            broadcast("User '" + disconnectedUsername + "' has left.");
        }
    }

    private static void broadcast(String message) {
        System.out.println(message);
        for (Socket clientSocket : connectedClients.keySet()) {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}