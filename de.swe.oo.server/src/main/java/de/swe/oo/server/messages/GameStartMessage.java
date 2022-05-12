package de.swe.oo.server.messages;

import de.swe.oo.server.player.Player;

public class GameStartMessage extends GameMessage {
    public GameStartMessage() {
        super("");
    }

    @Override
    public void handle(Player player) {
        if (player.getSession().startGame()) {
            return;
        } else {
            player.sendMessage(new GameAnnounceMessage("Couldn't start game. Probably too few players."));
        }
    }
}
