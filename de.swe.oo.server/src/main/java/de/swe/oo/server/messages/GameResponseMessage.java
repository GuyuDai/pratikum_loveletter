package de.swe.oo.server.messages;

import de.swe.oo.server.player.Player;

public class GameResponseMessage extends GameMessage{
    public GameResponseMessage(String messageText) {
        super(messageText);
    }

    @Override
    public void handle(Player player){
            player.processResponse(messageText);
    }
}
