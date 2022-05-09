package de.swe.oo.privateTest;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server_test {
    static ServerSocket serverSocket;
    static final int PORT = 1860;
    static ArrayList<Client_test> activeClients = new ArrayList<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for clients on port: " + PORT);

            while (true){
                Socket s = serverSocket.accept();
                Client_test client = new Client_test(s, activeClients);

                client.start();
            }
        } catch (Exception e) {
            System.out.println("Problem occurs on server part because: " + e.getMessage());
        }
    }
}







