package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

public class King extends Card {
    private static String NAME = "King";
    private static int VALUE = 6;

    public King(LoveLetterGame currentGame) {
        super("King", currentGame);
    }

    //Player may trade hands with other player
    @Override
    public void effect(){
        /** create nameList with all the Names of active Players */
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.sendMessage(new GameChoiceRequestMessage("Choose one of the names to trade decks", namelist));
        /** Chosen player needs to show his/her cards*/
        Player targetPlayer= currentGame.getPlayer(owner.getLastResponse());
        owner.sendMessage(new GameMessage(currentGame.choosePlayerDeck(targetPlayer)));
        owner.sendMessage(new GameMessage("Do you want to trade decks?"));
        /** If yes decks get changed*/
        if (owner.getLastResponse().equalsIgnoreCase("yes")){
            /** card1 and card 2 as temporary safed cards */
            Card card1= targetPlayer.getHands(0);
            Card card2= targetPlayer.getHands(1);
            targetPlayer.setHands(owner.getHands(0));
            targetPlayer.setHands(owner.getHands(1));
            owner.setHands(card1);
            owner.setHands(card2);
        }
    }
}
