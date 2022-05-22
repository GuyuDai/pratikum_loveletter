package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

/**
 * @author Franz
 */
public class GameMessage extends Message {

    public GameMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        System.out.println("Not implemented yet.");
    }

    public String output() {
        return "GAME " + messageText;
    }
}
