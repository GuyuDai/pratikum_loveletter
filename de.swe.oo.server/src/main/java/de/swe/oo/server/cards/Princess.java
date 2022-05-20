package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;

import java.util.jar.Attributes;

public class Princess extends Card {
    private static String NAME = "Princess";
    private static int VALUE = 8;

    public Princess(LoveLetterGame currentGame, Player owner ) {
        super("Princess",currentGame);
    }
    /** If the player plays or discards this card for any reason, they are eliminated from the round*/
    @Override
    public void effect(){
    this.currentGame.playerKickedOff(owner);
    }
}
