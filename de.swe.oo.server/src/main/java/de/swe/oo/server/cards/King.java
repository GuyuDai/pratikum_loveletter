package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class King extends Card {
    private static String NAME = "King";
    private static int VALUE = 6;

    public King(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    //Player may trade hands with other player
    @Override
    void discard(){

    }
}
