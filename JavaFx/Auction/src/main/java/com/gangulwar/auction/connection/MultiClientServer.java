package com.gangulwar.auction.connection;

import com.gangulwar.auction.controllers.BiddingDetailsWindowController;
import javafx.application.Platform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultiClientServer {

    private static final int PORT = 12345;
    private static Map<Socket, User> connectedClients = new HashMap<>();

    public static Map.Entry<String, Integer> HIGHEST_BID_USER = Map.entry("username", 0);
    public static BiddingDetailsWindowController biddingDetailsController;
    public static ArrayList<User> teamDetails = new ArrayList<>();

    public static void StartServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started. Waiting for clients...");

            new Thread(MultiClientServer::getTeamDetails).start();

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
            connectedClients.put(clientSocket, new User(username, 0));

            Platform.runLater(() -> {
                System.out.println("User '" + username + "' has joined.");
                String message=getUserPoints(username);
                broadcast(message);
            });


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
                clientObject.currentBid = Integer.parseInt(message);
                int bidValue = Integer.parseInt(message);
                if (bidValue > HIGHEST_BID_USER.getValue()) {
                    HIGHEST_BID_USER = Map.entry(clientObject.username, bidValue);
                    System.out.println("HIGHEST BID : " + HIGHEST_BID_USER.getValue() + " BY :" + HIGHEST_BID_USER.getKey());
                    if (biddingDetailsController != null) {
                        biddingDetailsController.updateBid("Team Name:" + HIGHEST_BID_USER.getKey() + "\nBid: " + HIGHEST_BID_USER.getValue());
                    }
                }


                System.out.println(clientObject.username + ": " + message);
//              broadcast(clientObject.username + ": " + finalMessage);


            }

        } catch (IOException e) {
            String disconnectedUsername = connectedClients.get(clientSocket).username;
            connectedClients.remove(clientSocket);
            broadcast("User '" + disconnectedUsername + "' has left.");
        }
    }

    private static void broadcast(String message) {
//        System.out.println(message);
        for (Socket clientSocket : connectedClients.keySet()) {
            try {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getTeamDetails() {
        String filePath = "src/main/java/com/gangulwar/auction/connection/teamDetails";

        try (BufferedReader reader = new BufferedReader(
                new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");

                teamDetails.add(
                        new User(
                                parts[0].trim(),
                                Integer.parseInt(parts[1].trim())
                        )
                );

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getUserPoints(String username) {

        for (User user : teamDetails) {
            if (user.username.equals(username)) {
                return "USER_CONNECTED "+username + ": " + user.points;
            }
        }

        return "USER_CONNECTED "+username + ": NOT_FOUND";
    }

    public static void startNewItemBid(String winnerDetails) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(
                        "src/main/java/com/gangulwar/auction/connection/winningTeamDetails",
                        true)
        )) {
            writer.write(winnerDetails);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        HIGHEST_BID_USER = Map.entry("no highest bid", 0);
        broadcast("WINNER: " + winnerDetails);
        System.out.println("WINNER: "+winnerDetails);
    }


}