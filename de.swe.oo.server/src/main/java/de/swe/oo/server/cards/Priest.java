package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class Priest extends Card {
    private static String NAME = "Priest";
    private static int VALUE = 2;

    public Priest(LoveLetterGame currentGame) {
        super("Priest", currentGame);
    }

    /**Player may privately see another players hand*/
    @Override
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.requestFromPlayer(new GameChoiceRequestMessage("Choose one of the names to look into their deck", namelist));
        /** Chosen player needs to show their cards*/
        currentGame.waitForAllResponses();
        /** Chosen player needs to show his/her cards*/
        int responseIndex1 = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex1]);
        owner.sendMessage(new GameMessage(currentGame.choosePlayerDeck(targetPlayer)));
        owner.sendMessage(new GameMessage("Do you want to look into their deck?"));
        /** If yes deck gets shown in chat*/
        if (owner.getLastResponse().equalsIgnoreCase("yes")){
            /** card1 and card 2 as temporary safed cards */
            Card card1= targetPlayer.getHands(0);
            Card card2= targetPlayer.getHands(1);
            owner.sendMessage(new GameMessage("The cards are" + card1 + "and" +card2));
        }
    }
}

