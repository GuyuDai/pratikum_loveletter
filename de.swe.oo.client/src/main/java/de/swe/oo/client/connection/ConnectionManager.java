package de.swe.oo.client.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Keeps references to all relevant objects of a TCP connection. Is created after successful login.
 */
public class ConnectionManager {
    private String ip;
    private int port;
    private String errorMessage;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    public ConnectionManager(String ip, int port) {
        this.ip = ip;
        this.port = port;
        this.errorMessage = "Nothing happened so far.";
    }

    public boolean loginAs(String name) {
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            errorMessage = "Error opening connection during login. " + e.getMessage();
            closeConnection();
            return false;
        }
        out.println(name);
        String answer;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            errorMessage = "Error receiving answer from server while logging in." + e.getMessage();
            closeConnection();
            return false;
        }
        if (!isValidLoginResponse(answer)) {
            errorMessage = "Error response by Server. Answer: " + answer;
            closeConnection();
            return false;
        } else {
            return true;
        }

    }

    private boolean isValidLoginResponse(String answer) {
        return answer.startsWith("SUCCESS");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BufferedReader getReader() {
        return in;
    }

    public PrintWriter getWriter() {
        return out;
    }

    public void closeConnection() {
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
            System.err.println("Error: Couldn't close connection. " + e.getMessage());
        }
    }
}
