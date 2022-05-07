package de.swe.oo.server.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Connection(Socket socket) {
        this.socket = socket;
        setupIO();
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
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Couldn't close the connection. " + e.getMessage());
        }
    }

    public String getLine() throws IOException {
        return in.readLine();
    }

    public synchronized void sendLine(String message) {
        notifyAll();
        out.println(message);
    }
}


