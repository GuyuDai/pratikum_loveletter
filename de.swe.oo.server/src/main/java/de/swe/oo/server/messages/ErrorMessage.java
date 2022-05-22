package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

/**
 * @author Franz
 * This class isn't really used that much currently. Especially the client just ignores ErrorMessages. It might be
 * interesting to make the client write error messages into some kind of log.
 */
public class ErrorMessage extends Message {

    public ErrorMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        System.out.println("An error occurred with player " + player.getName() +
                ". Message:" + this.messageText);
    }

    public String output() {
        return "ERROR " + messageText;
    }
}
