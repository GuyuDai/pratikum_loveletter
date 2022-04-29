package de.swe.oo.server.session;

import de.swe.oo.server.messages.ChatMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class LoginHandler extends Thread {
    private Session session;
    private int port;
    private int portOffset;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public LoginHandler(Session session, int port) {
        this.session = session;
        this.port = port;
        this.portOffset = 1;        //Used to give clients a unique port to connect to
    }

    @Override
    public void run() {
        while (true) {
            try {
                setupSocket();
            } catch (IOException e) {
                System.out.println("Error trying to accept connection." + e.getMessage());
                continue;
            }
            handleInput();
        }
    }

    private void setupSocket() throws IOException {
        acceptSocket();
        setupIO();
    }

    private void acceptSocket() {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();
        } catch (IOException e) {
            System.out.println("Opening login socket failed." +
                    e.getMessage()); // Logging framework is probably not worth it now
        }
    }

    private void setupIO() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    private void closeSocket() throws IOException {
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
    }

    private void handleInput() {
        // Awaits a line of text from the socket of the form "name", if such a message arrives
        // a player is created using these values and the socket is passed along.
        // In case of an invalid message the connection is closed and the client has to open a new one.
        String newName;
        try {
            newName = in.readLine();
        } catch (IOException e) {
            System.out.println("Error reading from socket. " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("Error parsing input message. " + e.getMessage());
            return;
        }

        if (session.isValidUsername(newName)) {
            acceptPlayer(newName);
        } else {
            out.println("ERROR Invalid Username");
            try {
                closeSocket();
            } catch (IOException e) {
                System.out.println("Couldn't close socket. " + e.getMessage());
            }
        }
    }

    private void acceptPlayer(String name) {
        int newPort = port + portOffset;
        out.println(newPort);
        Player newPlayer = new Player(session, name, newPort);
        session.addPlayer(newPlayer);
        ++portOffset;
        greet(newPlayer);
        announce(newPlayer);
        try {
            closeSocket();          //Try to prevent problems of access on the original socket later on
        } catch (IOException e) {
            System.out.println("Couldn't close the current socket. " + e.getMessage());
        }
    }

    private void greet(Player player) {
        Message msg = new ChatMessage("Willkommen " + player.name + ".");
        player.sendMessage(msg);
    }

    private void announce(Player newPlayer) {
        for (Player player : session.players) {
            if (player.name != newPlayer.name) {
                Message msg = new ChatMessage("Der Spieler " +
                        newPlayer.name +
                        " hat den Raum betreten.");
                player.sendMessage(msg);
            }
        }
    }
}
