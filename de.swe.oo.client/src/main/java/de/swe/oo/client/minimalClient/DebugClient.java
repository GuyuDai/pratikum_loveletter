package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class DebugClient extends Client {
    BufferedReader userIn;
    PrintWriter userOut;
    ConnectionManager connectionManager;
    ConnectionListener connectionListener;
    PrintWriter connectionOut;
    UserInputListener userInputListener;

    public DebugClient(String ip, int port, BufferedReader userIn, PrintWriter out) {
        this.userIn = userIn;
        this.userOut = out;
        this.connectionManager = new ConnectionManager(ip, port);
    }

    @Override
    public void run() {
        login();
        setupConnectionObjects();
        connectionListener.start();
        connectionListener.setName("connectionListener");
        userInputListener.start();
        userInputListener.setName("userInputListener");
        try {
            connectionListener.join();
            userInputListener.join();
        } catch (InterruptedException e) {
            System.err.println("Error, client was interrupted. " + e.getMessage());
        }
    }

    public void login() {
        String input;
        boolean loggedIn = false;
        while (!loggedIn) {
            outputChat("Bitte Nutzernamen eingeben:");
            try {
                input = userIn.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from console. " + e.getMessage());
                continue;
            }
            loggedIn = connectionManager.loginAs(input);
            if (!loggedIn) {
                userOut.println(connectionManager.getErrorMessage());
            }
        }
    }

    public void setupConnectionObjects() {
        connectionOut = connectionManager.getWriter();
        connectionListener = new ConnectionListener(this, connectionManager.getReader());
        userInputListener = new UserInputListener(this, userIn);
    }

    public void sendText(String text) {
        connectionOut.println(text);
    }

    public void outputChat(String text) {
        userOut.println(text);
    }

    public void close() {
        // Problem: userInputListener locks the BufferedReader by calling readLine, thus it can't be closed easily.
        connectionListener.close();
        userInputListener.close();
        connectionManager.closeConnection();
    }

    public static void main(String[] args) {
        DebugClient client = new DebugClient("localhost", 4444,
                new BufferedReader(new InputStreamReader(System.in)),
                new PrintWriter(System.out, true));
        client.start();
    }
}
