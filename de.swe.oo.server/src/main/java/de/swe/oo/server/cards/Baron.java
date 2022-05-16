package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameInputRequestMessage;
import de.swe.oo.server.messages.GameResponseMessage;
import de.swe.oo.server.player.Player;

public class Baron extends Card {
    private static String NAME = "Baron";
    private static int VALUE = 3;

    public Baron(LoveLetterGame currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    /**Player may choose another player and privately compare hands. The player with the lower-value card is eliminated.*/
    @Override
    void discard(){
    this.currentGame.getNameOfActivePlayers();
    owner.sendMessage(new GameResponseMessage("Choose one of the above Names to compare deck"));
    //this.currentGame.getPlayer(targetplayername);

    }
}

