package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class Baron extends Card {
    private static String NAME = "Baron";
    private static int VALUE = 3;

    public Baron(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    //Player may choose another player and privately compare hands. The player with the lower-value card is eliminated.
    @Override
    void discard(){

    }
}

