package de.swe.oo.client.connection;

import de.swe.oo.client.Client;
import de.swe.oo.client.Listener;

import java.io.BufferedReader;

/**
 * Listens to a TCP-connection and outputs all incoming messages directly on the chat.
 */
public class ConnectionListener extends Listener {

    public ConnectionListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    protected void handleInput(String input) {
        client.outputChat(input);
    }
}
