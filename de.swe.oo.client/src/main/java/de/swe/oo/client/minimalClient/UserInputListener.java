package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class UserInputListener extends Listener {

    InputStreamReader isrForCheckingInput;
    public UserInputListener(Client client, BufferedReader reader, InputStreamReader isr) {
        super(client, reader);
        this.isrForCheckingInput = isr;
    }


    @Override
    void handleInput(String input) {
        client.sendText(input);
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                while(isrForCheckingInput.ready()){
                    readMessage();
                }
            } catch (IOException e) {
                System.err.println("Error while trying to read message inside UserInputlistener. " + e.getMessage());
                client.close();
            }
        }
    }
}

