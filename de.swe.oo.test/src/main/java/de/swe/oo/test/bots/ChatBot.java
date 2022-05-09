package de.swe.oo.test.bots;

import de.swe.oo.client.Client;
import de.swe.oo.client.connection.ConnectionListener;
import de.swe.oo.client.connection.ConnectionManager;

import java.io.PrintWriter;
import java.util.Random;

public class ChatBot extends Client {
    String ip;
    int port;
    private String name;
    private String[] possibleMessages;
    private ConnectionManager connectionManager;
    private ConnectionListener connectionListener;
    private PrintWriter connectionOut;
    private String lastChatMessage;
    private boolean isRunning;
    private int minIntervalInMs;
    private int maxIntervalInMs;
    private int logoutChancePercent;

    public ChatBot(String ip, int port, String name, String[] possibleMessages, int minIntervalInMs, int maxIntervalInMs, int logoutChancePercent) {
        this.ip = ip;
        this.port = port;
        this.name = name;
        this.possibleMessages = possibleMessages;
        this.minIntervalInMs = minIntervalInMs;
        this.maxIntervalInMs = maxIntervalInMs;
        this.logoutChancePercent = logoutChancePercent;
        isRunning = true;
    }

    @Override
    public void run() {
        connectionManager = new ConnectionManager(ip, port);
        if (connectionManager.loginAs(name)) {
            connectionListener = new ConnectionListener(this, connectionManager.getReader());
            connectionOut = connectionManager.getWriter();
        } else {
            System.err.println(
                    "Bot couldn't log in with name "
                            + name
                            + ". Error: "
                            + connectionManager.getErrorMessage());
            return;
        }
        while (isRunning) {
            sendText("CHAT " + getRandomText());
            try {
                sleep(getSleepTime());
            } catch (InterruptedException e) {
                System.err.println("Something went wrong while waiting. " + e.getMessage());
            }
            tryRandomExitWithPercentChance(logoutChancePercent);
        }
    }

    private String getRandomText() {
        int rnd = new Random().nextInt(possibleMessages.length);
        return possibleMessages[rnd];
    }

    private int getRandomBetween(int min, int max) {
        return new Random().nextInt(min, max);
    }

    private int getSleepTime() {
        return getRandomBetween(minIntervalInMs, maxIntervalInMs);
    }

    private void tryRandomExitWithPercentChance(int percent) {
        int result = getRandomBetween(0, 101);
        if (result < percent) {
            sendText("CHAT bye");
        }
    }

    public String getLastChatMessage() {
        return lastChatMessage;
    }

    @Override
    public void sendText(String text) {
        connectionOut.println(text);
    }

    @Override
    public void outputChat(String text) {
        lastChatMessage = text;
    }

    @Override
    public void close() {
        System.err.println("Bot " + name + " shutting down.");
        isRunning = false;
    }
}
