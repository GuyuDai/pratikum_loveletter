package de.swe.oo.privateTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread_test extends Thread {

    private static Socket clientSocket;
    private static PrintWriter output;
    private static BufferedReader input;
    static Thread thread;
    //private static ObservableList<String> messageLog = FXCollections.observableArrayList();
    //private static ObservableList<String> clientNames = FXCollections.observableArrayList();
    private static String name;
    private static final int SERVER_PORT = 1860;
    private static final String SERVER_IP = "localhost";

    public static void main(String[] args) {
        try {
            connectDatabase();
            registerClient();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public static void connectDatabase() {
        try {
            System.out.println("Creating a socket on: " + SERVER_IP + ":" + SERVER_PORT);
            clientSocket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connection to the server on: " + SERVER_IP + ":" + SERVER_PORT);
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            thread = new ClientThread_test();
            thread.start();

        } catch (IOException e) {
            System.out.println("error thrown during connection");
            e.getMessage();
        }
    }

    public static void registerClient() throws IOException {

        System.out.println("Please enter your username: ");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        name = reader.readLine();
        output.println(name);
    }



    public void run() {
        while (isAlive()) {
            try {
                System.out.println(input.readLine());
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

}
