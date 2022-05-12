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
        if (input.length() < 4){
            return new ErrorMessage("Message too short.");
        }
        Message result;
        String type = input.substring(0, 4);
        String text = "";
        if (input.length() > 5) {
            text = input.substring(5);
        }
        switch (type) {
            case "CHAT":
                result = new ChatMessage(text);
                break;
            case "GAME":
                result = parseGameMessage(text);
                break;
            case "EXIT":
                result = new ExitMessage();
                break;
            default:
                result = new ErrorMessage("Unknown Type.");
                break;
        }
        return result;
    }

    private GameMessage parseGameMessage(String gameMessageText){
        if (gameMessageText.startsWith("CREATE")){
            return new GameCreateMessage("");
        }
        if (gameMessageText.startsWith("RESPONSE")){
            return new GameResponseMessage(gameMessageText.substring(9));  //Remove RESPONSE and one space
        }
        // Default case, shouldn't actually be reached.
        return new GameMessage(gameMessageText);
    }

    public void close() {
        this.running = false;
    }
}

