package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;

public class Prince extends Card {
    private static String NAME = "Prince";
    private static int VALUE = 5;

    public Prince(LoveLetterGame currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    // Player may choose any player (including themselves) to discard their hand and draw a new one
    @Override
    void discard(){

    }
}
