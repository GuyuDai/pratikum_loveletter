package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

public class ErrorMessage extends Message {

    public ErrorMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        System.out.println("An error occurred with player " + player.name +
                ". Message:" + this.messageText);
    }

    public String output() {
        return "ERROR " + messageText;
    }
}
