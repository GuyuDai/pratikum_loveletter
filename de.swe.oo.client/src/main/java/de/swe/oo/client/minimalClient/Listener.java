package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;

public class Listener extends Thread {
    BufferedReader reader;
    boolean running;

    public Listener(BufferedReader reader) {
        this.reader = reader;
        this.running = true;
    }

    @Override
    public void run() {
        String input = "";
        while (running && input != null) {
            try {
                input = reader.readLine();
            } catch (IOException e) {
                System.out.println("Listener couldn't get message. " + e.getMessage());
                running = false;
            }
            if (running) {
                System.out.println(parseInput(input));
            }
        }
        running = false;
    }

    private String parseInput(String input) {
        String result = input;
        return result;
    }

    public void close() {
        this.running = false;
    }
}
