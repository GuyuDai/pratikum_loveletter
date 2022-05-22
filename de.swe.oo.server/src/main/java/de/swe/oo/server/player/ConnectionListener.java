package de.swe.oo.server.player;


import de.swe.oo.server.messages.*;

import java.io.IOException;

/**
 * This class does all the message parsing. It receives any messages to a socket asynchronously and initiates the
 * appropriate actions. This is done by first parsing the input String to get a Message and then calling the handle
 * method of the Message with the player associated with the listener as an argument.
 */
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
            return new GameCreateMessage();
        }
        if (gameMessageText.startsWith("RESPONSE")){
            return new GameResponseMessage(gameMessageText.substring(9));  //Remove RESPONSE and one space
        }
        if (gameMessageText.startsWith("JOIN")){
            return new GameJoinMessage();
        }
        if (gameMessageText.startsWith("START")){
            return new GameStartMessage();
        }
        if (gameMessageText.startsWith("STATUS")){
            return new GameStatusRequestMessage();
        }
        if (gameMessageText.startsWith("SCORE")){
            return new GameScoreRequestMessage();
        }
        // Default case, shouldn't actually be reached.
        return new GameMessage(gameMessageText);
    }

    public void close() {
        this.running = false;
    }
}

