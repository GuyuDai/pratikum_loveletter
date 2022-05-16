package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class Countess extends Card {
    private static String NAME = "Countess";
    private static int VALUE = 7;

    public Countess(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    /** If the player holds this card either and the King or the Prince, this card must be played immediately, which otherwise does nothing*/
    @Override
     void discard(){

    }
}
