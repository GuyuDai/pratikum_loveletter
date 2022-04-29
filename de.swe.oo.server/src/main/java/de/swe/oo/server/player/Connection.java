package de.swe.oo.server.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection extends Thread {
    /**
     * Diese Klasse ist dazu gedacht, für einen neu erstellten Spieler eine Verbindung aufzubauen. Dazu wird auf einem
     * port gewartet, bis der client sich mit dem ihm mitgeteilten port verbindet. Eindeutige ports werden durch die
     * Variable portOffset in LoginHandler "sichergestellt".
     */
    Player player;
    ServerSocket serverSocket;
    public boolean isSetUp;
    private Socket socket;
    private int port;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(int port) {
        this.port = port;
        isSetUp = false;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Opening player socket failed." +
                    e.getMessage()); // Logging framework is probably not worth it now
        }
        setupIO();
        isSetUp = true;
    }

    private void setupIO() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            serverSocket.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Couldn't close the connection. " + e.getMessage());
        }
    }

    public String getLine() throws IOException {
        return in.readLine();
    }

    public void sendLine(String message) {
        while (!isSetUp) {
            try {
                sleep(15);
            } catch (Exception e) {
                System.out.println("Problem while waiting for the connection to be set up. " + e.getMessage());
            }
        }
        out.println(message);
    }
}


