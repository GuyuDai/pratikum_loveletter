package de.swe.oo.server.cards;


import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class Handmaid extends Card {
    private static String NAME = "Handmaid";
    private static int VALUE = 4;

    public Handmaid(Game currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    // Player cannot be affected by any other player´s cards until their next turn
    @Override
    void discard(){

    }
}
