package com.gangulwar.auction.connection;

import com.gangulwar.auction.controllers.BiddingDetailsWindowController;

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

    public static Map.Entry<String, Integer> HIGHEST_BID_USER = Map.entry("username", 0);
    public static BiddingDetailsWindowController biddingDetailsController;
    public static void StartServer() {
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
            connectedClients.put(clientSocket, new User(username, "0", 0));

            broadcast("User '" + username + "' has joined.");

            new Thread(() -> handleClientInteraction(clientSocket, reader, writer)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClientInteraction(Socket clientSocket, BufferedReader reader, PrintWriter writer) {
        try {
            User clientObject = connectedClients.get(clientSocket);

            String message;
            while ((message = reader.readLine()) != null) {
                clientObject.currentBid = message;
                int bidValue = Integer.parseInt(message);
                if (bidValue > HIGHEST_BID_USER.getValue()) {
                    HIGHEST_BID_USER = Map.entry(clientObject.username, bidValue);
                    System.out.println("HIGHEST BID : " + HIGHEST_BID_USER.getValue() + " BY :" + HIGHEST_BID_USER.getKey());
                    if (biddingDetailsController != null) {
                        biddingDetailsController.updateBid("Team Name:" + HIGHEST_BID_USER.getKey() + "\nBid: " + HIGHEST_BID_USER.getValue());
                    }
                }
                broadcast(clientObject.username + ": " + message);
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