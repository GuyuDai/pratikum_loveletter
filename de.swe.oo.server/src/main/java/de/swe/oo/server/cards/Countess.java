package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

/**
 * @auther Dai, Nik, Nargess, Minghao, Nassrin
 */
public class Countess extends Card {
    private static String NAME = "COUNTESS";
    private static int VALUE = 7;

    public Countess(LoveLetterGame currentGame) {
        super("COUNTESS", currentGame);
    }

    public void effect(){
        /** If the player holds this card either and the King or the Prince,
         * this card must be played immediately, which otherwise does nothing
         * @Author Nik*/
        String[] handsDeck = this.owner.showHands();
        for (String s : handsDeck) {
            if (s.contains("KING") || s.contains("PRINCE")) {
                effect();
            }
        }
            owner.sendMessage(new GameMessage("This card was played because you had a Prince or a King in your Hands."));
    }
}

