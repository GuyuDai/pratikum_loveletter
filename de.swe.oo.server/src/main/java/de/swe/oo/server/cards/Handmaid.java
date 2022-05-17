package de.swe.oo.server.cards;


import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

public class Handmaid extends Card {
    private static String NAME = "Handmaid";
    private static int VALUE = 4;

    public Handmaid(LoveLetterGame currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    // Player cannot be affected by any other player´s cards until their next turn
    @Override
    void discard(){
        owner.setImmune(true); //TODO: how do we implement the immunity? this needs to get asked before any round
        owner.sendMessage(new GameMessage(owner.getName() + " is now immune to any Effects!"));
        currentGame.removeCurrentPlayer(owner); //removes the current player for now, as he is immune (currentplayerlist only stores player which can be effected)
    }
}
