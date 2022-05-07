package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;


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
    public synchronized void run() {
        while (isRunning) {
            String input;
            try {
                wait();
                input = reader.readLine();
                if (input == null) {
                    throw new IOException("EOF Error BufferedReader returned null.");
                }
                if (input.equals("bye")){
                    System.out.println("Goodbye:"+this.getName());
                    client.close();
                }
                handleInput(input);
            } catch (IOException e) {
                System.err.println("Error while trying to read message inside listener. " + e.getMessage());
                client.close();
            }catch (InterruptedException e) {
                System.out.println("InterruptedException in Listener" + e.getMessage());
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
