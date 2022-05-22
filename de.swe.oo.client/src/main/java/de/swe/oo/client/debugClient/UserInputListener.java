package de.swe.oo.client.debugClient;

import de.swe.oo.client.Client;
import de.swe.oo.client.Listener;

import java.io.BufferedReader;

/**
 * Receives messages arriving from the user (i.e. typically a BufferedReader wrapping the terminal) and immediately
 * send the input as received to the server.
 */
public class UserInputListener extends Listener {
    public UserInputListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    public void handleInput(String input) {
        client.sendText(input);
    }
}
