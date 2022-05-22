package de.swe.oo.server.cards;


import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

public class Handmaid extends Card {
    private static String NAME = "Handmaid";
    private static int VALUE = 4;

    public Handmaid(LoveLetterGame currentGame) {
        super("Handmaid", currentGame);
    }

    /** Player cannot be affected by any other player´s cards until their next turn*/
    @Override
    public void effect(){
        owner.setIsProtected();
        currentGame.sendToAllPlayers(new GameAnnounceMessage
            (owner.getName() + " is now immune to any Effects!"));
        /**removes the current player for now, as he is immune (currentplayerlist only stores player which can be effected)*/
        //currentGame.removeCurrentPlayer(owner);
    }
}

