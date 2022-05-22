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


/**
 * Handles the login "protocol". Once a client connects a potential name is immediately expected as input. This is
 * checked for availability. If the desired name isn't already taken a new player is created with the socket connected
 * to the client. If the name is already taken the connection is closed and the client needs to connect again to try
 * a different name.
 */
public class LoginHandler extends Thread {
    private Session session;
    private int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public LoginHandler(Session session, int port) {
        this.port = port;
        this.session = session;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Error while trying to connect." + e.getMessage());
            return;
        }
        while (true) {
            try {
                prepareSocket();
            } catch (IOException e) {
                System.out.println("Error trying to accept connection." + e.getMessage());
                continue;
            }
            getAndHandleInput();
        }
    }

    private void prepareSocket() throws IOException {
        socket = serverSocket.accept();
        setupIO();
    }


    private void setupIO() throws IOException {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /* Awaits a line of text from the socket of the form "name", if such a message arrives
     * a player is created using these values and the socket is passed along to the Player constructor
     * and a  message of the form "SUCCESS" is sent to the connecting client.
     * In case of an invalid message the connection is closed and the client has to open a new one.
     */
    private void getAndHandleInput() {
        String newName;
        try {
            newName = in.readLine();
        } catch (IOException e) {
            System.out.println("Error reading from socket. " + e.getMessage());
            return;
        }

        if (session.checkIfValidUsername(newName)) {
            acceptPlayer(newName);
        } else {
            out.println("This username does already exist!");
            try {
                abortCurrentSocket();
            } catch (IOException e) {
                System.out.println("Couldn't close socket. " + e.getMessage());
            }
        }
    }

    private void abortCurrentSocket() throws IOException {
        if (in != null) {
            in.close();
        }
        if (out != null) {
            out.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    private void acceptPlayer(String name) {
        out.println("SUCCESS");
        Player newPlayer = new Player(session, name, socket);
        session.addPlayer(newPlayer);
        greet(newPlayer);
        announce(newPlayer);
    }

    private void greet(Player player) {
        Message msg = new ChatMessage("Welcome " + player.getName() + ".");
        player.sendMessage(msg);
    }

    private void announce(Player newPlayer) {
        for (Player player : session.players) {
            if (player.getName() != newPlayer.getName()) {
                Message msg = new ChatMessage(newPlayer.getName() + " joined the room.");
                player.sendMessage(msg);
            }
        }
    }
}
