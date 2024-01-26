package com.gangulwar.auction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TestClient {

    private static String SERVER_IP;
    private static final int SERVER_PORT = 12345;
    private static PrintWriter writer;
    private static BufferedReader serverReader;

    public TestClient(String serverIP,String username) throws IOException {
        SERVER_IP=serverIP;
        Socket socket = new Socket(SERVER_IP, SERVER_PORT);
        serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in));
        writer.println(username);
        new Thread(() -> handleServerMessages(serverReader)).start();
    }

    /*public static void main(String[] args) {
        try {


            System.out.print("Enter your username: ");
            String username = userInputReader.readLine();





            String message;
            while ((message = userInputReader.readLine()) != null) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public static void sendMessagetoServer(String message){
        writer.println(message);
    }
    private static void handleServerMessages(BufferedReader serverReader) {
        try {
            String message;
            while ((message = serverReader.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


