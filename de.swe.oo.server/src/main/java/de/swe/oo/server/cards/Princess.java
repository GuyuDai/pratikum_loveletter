package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;

public class Princess extends Card{
    private static String NAME = "Princess";
    private static int VALUE = 7;

    public Princess(LoveLetterGame currentGame) {
        super(NAME, currentGame);
    }

    @Override
    public void effect(){
        this.currentGame.playerKickedOff(this.owner);
    }
}
