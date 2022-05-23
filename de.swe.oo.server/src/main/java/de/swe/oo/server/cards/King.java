package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class King extends Card {
    private static String NAME = "KING";
    private static int VALUE = 6;

    public King(LoveLetterGame currentGame) {
        super("KING", currentGame);
    }

    /**Player may trade hands with other player
     * @Author Nik,Minghao Li*/
    @Override
    public void effect(){
        /** create nameList with all the Names of active Players */
        String [] nameList = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.requestFromPlayer(new GameChoiceRequestMessage("Choose one player to trade your cards!", nameList));
        currentGame.waitForAllResponses();
        /** Chosen player needs to show his/her cards*/
        int responseIndex1 = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(nameList[responseIndex1]);
        if (targetPlayer==null){
            owner.sendMessage(new GameAnnounceMessage("this player is protected,so your card have no effect"));
            return;
        }
        /** Card King is already used so can’t be traded，trade another card in owner‘hand to targetPlayer*/
            Card card1= targetPlayer.getHands(0);
            int i=0;
            if(owner.getHands(i).getName().equals("KING")){
                targetPlayer.setHands(owner.getHands(i+1),i);
                owner.setHands(card1,i+1);

        }
        else  targetPlayer.setHands(owner.getHands(i),i);
               owner.setHands(card1,i);
    }
}

