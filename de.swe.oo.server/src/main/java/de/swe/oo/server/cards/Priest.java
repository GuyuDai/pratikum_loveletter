package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class Priest extends Card {
    private static String NAME = "Priest";
    private static int VALUE = 2;

    public Priest(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    //Player may privately see another players hand
    @Override
    void discard(){

    }
}
