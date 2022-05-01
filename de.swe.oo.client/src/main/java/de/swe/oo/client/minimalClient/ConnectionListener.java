package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;

public class ConnectionListener extends Listener {

    public ConnectionListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    void handleInput(String input) {
        client.outputChat(input);
    }
}
