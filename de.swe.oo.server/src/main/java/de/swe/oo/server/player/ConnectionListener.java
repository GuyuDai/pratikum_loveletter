package de.swe.oo.server.player;


import de.swe.oo.server.messages.*;

import java.io.IOException;

public class ConnectionListener extends Thread {
    private boolean running;
    private Player player;

    public ConnectionListener(Player player) {
        this.player = player;
        this.running = true;
    }

    @Override
    public void run() {
        String input;
        Message msg;
        while (running) {
            try {
                input = player.connection.getLine();
            } catch (IOException e) {
                System.out.println("Listener couldn't get message. " + e.getMessage());
                running = false;
                player.quit();
                continue;
            }
            msg = parseInput(input);
            msg.handle(player);
        }
    }

    private Message parseInput(String input) {
        if (input.length() < 5) {
            return new ErrorMessage("Message too short. At least 4 letters for message " +
                    "Type and one space are needed.");
        }
        Message result;
        String type = input.substring(0, 4);
        String text = input.substring(5);
        switch (type) {
            case "CHAT":
                result = new ChatMessage(text);
                break;
            case "GAME":
                result = new GameMessage(text);
                break;
            default:
                result = new ErrorMessage("Unknown Type.");
                break;
        }
        return result;
    }

    public void close() {
        this.running = false;
    }
}
