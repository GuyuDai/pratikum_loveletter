package de.swe.oo.client.minimalClient;

import de.swe.oo.client.Client;
import de.swe.oo.client.Listener;

import java.io.BufferedReader;

public class UserInputListener extends Listener {
    public UserInputListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    public void handleInput(String input) {
        client.sendText(input);
    }
}
