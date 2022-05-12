package de.swe.oo.server.messages;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class GameJoinMessage extends GameMessage {
    public GameJoinMessage() {
        super("");
    }

    @Override
    public void handle(Player player) {
        if (!player.getSession().gameExists()) {
            player.sendMessage(new GameAnnounceMessage("Couldn't join Game, it hasn't been created yet."));
            return;
        }
        if (player.getSession().joinGame(player)) {
            player.getSession().broadcast(new GameAnnounceMessage(player.getName() + " joined the game."));
        } else {
            player.sendMessage(new GameAnnounceMessage("Game refused join. Probably already full."));
        }
    }

}
