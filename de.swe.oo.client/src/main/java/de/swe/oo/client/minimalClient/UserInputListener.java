package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;

public class UserInputListener extends Listener {
    public UserInputListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    void handleInput(String input) {
        client.sendText(input);
    }
}
