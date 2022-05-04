package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;

abstract public class Listener extends Thread {
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
                input = reader.readLine();
                Boolean flag = true;
                while(flag){
                    sleep(20);
                    if (input == null) {
                        flag = false;
                        isRunning = false;  //whether this line is needed?
                        break;
                    }
                    handleInput(input);
                }
            } catch (IOException e) {
                System.err.println("Error while trying to read message inside listener. " + e.getMessage());
                client.close();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
