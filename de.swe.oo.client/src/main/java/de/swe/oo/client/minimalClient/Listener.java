package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public abstract class Listener extends Thread {
    Client client;
    BufferedReader reader;
    public boolean isRunning;


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
                if(reader.ready()){
                    input = reader.readLine();
                    //if(Objects.equals(input, " ")){   //for testing
                    if (input == null) {
                        throw new IOException("EOF Error BufferedReader returned null.");
                    }
                    //System.out.println(input);    //for testing
                    handleInput(input);
                }
            } catch (IOException e) {
                System.err.println("Error while trying to read message inside listener. " + e.getMessage());
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


    abstract void handleInput(String input);
}
