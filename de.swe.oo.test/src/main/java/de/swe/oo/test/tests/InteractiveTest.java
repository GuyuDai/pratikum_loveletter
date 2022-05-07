package de.swe.oo.test.tests;

import de.swe.oo.client.connection.ConnectionManager;
import de.swe.oo.server.session.Session;
import de.swe.oo.test.bots.ChatBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Thread.sleep;

public class InteractiveTest {
    public static void main(String[] args) {
        String HOST = "localhost";
        int PORT = 4444;
        int minWait = 500;
        int maxWait = 2000;
        int logoutChancePercent = 20;
        String[] NAMES = {"Jannik", "Korbin", "Gerhard", "Luisa", "Anna", "Jaqueline"};
        String[] MESSAGES = {"Hallo", "Hey", "Toller Chat.", "Das ist ein wirklich guter Chat."};
        LinkedList<ChatBot> botList = new LinkedList<ChatBot>();

        Session session = new Session(PORT);
        session.start();
        try {
            sleep(200);  //Bots currently just shut down when the login doesn't work, so we have to wait for
                                // the server to be ready before we log in.
        } catch (InterruptedException e) {
            System.err.println("Error trying to sleep while setting up server. " + e.getMessage());
        }
        for (String name : NAMES) {
            ChatBot newBot = new ChatBot(HOST, PORT, name, MESSAGES, minWait, maxWait, logoutChancePercent);
            newBot.start();
            botList.add(newBot);
        }
        ConnectionManager connectionManager = new ConnectionManager(HOST, PORT);
        if (connectionManager.loginAs("Observer")) {
            BufferedReader in = connectionManager.getReader();
            String input = "";
            try {
                while (input != null) {
                    System.out.println(input);
                    input = in.readLine();
                }
            } catch (IOException e) {
                System.out.println("Couldn't show Chat messages.");
            }
        }
    }
}
