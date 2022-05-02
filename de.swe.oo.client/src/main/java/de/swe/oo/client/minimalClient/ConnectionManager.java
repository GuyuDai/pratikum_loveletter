package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.Thread.sleep;

public class ConnectionManager {
    private Client client;
    private String ip;
    private int port;
    private String errorMessage;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private boolean successfulLogin;

    public ConnectionManager(String ip, int port, Client client){
        this.ip = ip;
        this.port = port;
        this.client = client;
        this.errorMessage = "Nothing happened so far.";
        this.successfulLogin = false;
    }

    public boolean loginAs(String name) {
        return uglyLogin(name);
        /* This is the actual implementation when the login procedure is fixed on the server
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            errorMessage = "Error opening Connection during login. " + e.getMessage();
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

         */
    }

    private boolean isValidLoginResponse(String answer) {
        return !answer.startsWith("ERRO");
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

    public boolean isClosed() {
        return socket.isClosed();
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

    public boolean uglyLogin(String name) {
        // Needs to stay until login procedure of server is fixed
        try {
            socket = new Socket(ip, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("An error occurred while opening IO objects for login. " + e.getMessage());
            return false;
        }
        out.println(name);
        String answer;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving the User port. " + e.getMessage());
            return false;
        }
        if (!isValidLoginResponse(answer)){
            return false;
        }
        int finalPort = Integer.parseInt(answer);
        if (finalPort < 0) {
            System.err.println("An error occured while requesting port.");
            return false;
        }
        int errorCount = 0;
        while (true) {
            try {
                sleep(20);
                socket = new Socket(ip, finalPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                break;
            } catch (IOException e) {
                System.err.println("An error occurred while opening IO objects. " + e.getMessage());
                ++errorCount;
                if (errorCount > 15) {
                    return false;
                }
            } catch (Exception e) {
                System.err.println("Error while connecting to final port. " + e.getMessage());
                ++errorCount;
                if (errorCount > 15) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSuccessfulLogin() {
        return successfulLogin;
    }

    public void setSuccessfulLogin(boolean successfulLogin) {
        this.successfulLogin = successfulLogin;
    }
}
