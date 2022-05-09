package de.swe.oo.client;

import de.swe.oo.client.Client;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Listener extends Thread {
    protected Client client;
    private BufferedReader reader;


    private boolean isRunning;

    public boolean isRunning() {
        return isRunning;
    }

    public Listener(Client client, BufferedReader reader) {
        this.client = client;
        this.reader = reader;
        isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            String input;
            try {
                input = reader.readLine();
                if (input == null) {
                    throw new IOException("EOF Error BufferedReader returned null.");
                }
                handleInput(input);
            } catch (IOException e) {
                System.err.println("Error while trying to read message inside listener. " + e.getMessage());
                close();
                client.close();
            }
        }
    }

    public void close() {
        isRunning = false;
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("Error while closing a listener socket." + e.getMessage());
        }
    }

    protected abstract void handleInput(String input);
}
