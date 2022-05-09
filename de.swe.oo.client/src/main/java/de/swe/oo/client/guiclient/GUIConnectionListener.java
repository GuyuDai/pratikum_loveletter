package de.swe.oo.client.guiclient;

import de.swe.oo.client.Client;
import de.swe.oo.client.Listener;

import java.io.BufferedReader;

public class GUIConnectionListener extends Listener {
    public GUIConnectionListener(Client client, BufferedReader reader) {
        super(client, reader);
    }

    @Override
    protected void handleInput(String input) {
        if (input.startsWith("CHAT")){
            client.outputChat(input.substring(5));
        }
    }
}
