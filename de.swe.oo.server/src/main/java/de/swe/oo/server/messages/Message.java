package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

abstract public class Message {
    String messageText;

    abstract public void handle(Player player);

    abstract public String output();
}
