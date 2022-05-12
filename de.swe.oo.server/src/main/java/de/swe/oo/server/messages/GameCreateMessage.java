package de.swe.oo.server.messages;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class GameCreateMessage extends GameMessage {
    public GameCreateMessage() {
        super("");
    }

    @Override
    public void handle(Player player) {
        if (player.getSession().gameExists()) {
            player.sendMessage(new GameAnnounceMessage("Game already exists."));
            return;
        } else {
            player.getSession().createNewGame();
            player.getSession().broadcast(new GameAnnounceMessage(player.getName() + " created a new Game."));
        }
    }
}
