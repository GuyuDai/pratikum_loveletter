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
            client.outputChat(input.substring(5));  //beginIndex is length of CHAT + 1 for space
            return;
        }
        if (input.startsWith("GAME ANNOUNCE")){
            client.outputChat("Game Announcement: " + input.substring(14));
            return;
        }
        if (input.startsWith("GAME REQUEST INPUT")){
            client.outputChat("Game Request: " + input.substring(19));
        }
        if (input.startsWith("GAME REQUEST CHOICE")){
            String text = input.substring(20);
            String[] args = text.split(" ");
            client.outputChat("Game Request: " + unEncodedSpaces(args[0]));
            for (int i=1; i<args.length; ++i){    //The prompt is in the first slot
                client.outputChat("Choice " + (i-1) +": " + unEncodedSpaces(args[i]));
            }
            client.outputChat("Please enter the desired number");
        }
    }
    private String unEncodedSpaces(String in){
        return in.replace(";", " ");
    }
}
