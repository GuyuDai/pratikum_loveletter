package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class Guard extends Card{
    private static String NAME = "Guard";
    private static int VALUE = 1;

    public Guard(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    @Override
    void discard(){

    }
}
