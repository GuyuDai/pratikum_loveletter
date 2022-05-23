package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

/**
 * @auther Dai, Nik, Nargess, Minghao, Nassrin
 */
public class Baron extends Card {
    private static String NAME = "BARON";
    private static int VALUE = 3;

    public Baron(LoveLetterGame currentGame) {
        super("BARON", currentGame);
    }

    /**Player may choose another player and privately compare hands. The player with the lower-value card is eliminated.
     * @Author Nik ,Minghao Li*/
    @Override
    public void effect(){
    /** create nameList with all the Names of active Players */
    String [] nameList = currentGame.getNameOfActivePlayers().toArray(new String[0]);

    /** Player can choose a name to show their hands*/
    owner.requestFromPlayer(new GameChoiceRequestMessage("Choose one of the names to compare your cards", nameList));
    currentGame.waitForAllResponses();

    /** Chosen player needs to show his/her cards*/
    int responseIndex1 = parseInt(owner.getLastResponse().trim());
    Player targetPlayer= currentGame.getPlayer(nameList[responseIndex1]);
        if (targetPlayer==null){
            owner.sendMessage(new GameAnnounceMessage("this player is protected,so your card have no effect"));
            return;
        }
    /** compare hands and eliminate player with lower hand*/
    Card targetPlayerCardOne= targetPlayer.getHands(0);
    Card ownerCardOne= owner.getHands(0);
    Card ownerCardTwo= owner.getHands(1);

    /** The player with the smaller hand looses */
     int ownerCardValue = ownerCardOne.getValue()+ ownerCardTwo.getValue()-3;

     /** AddValue of two Cards - Baron's value(3) is left card's value.*/
     int targetPlayerCardValue= targetPlayerCardOne.getValue();
     if (ownerCardValue<targetPlayerCardValue){
        currentGame.playerKickedOff(owner);
    } else if(ownerCardValue>targetPlayerCardValue){
        currentGame.playerKickedOff(targetPlayer);
    }
     else owner.sendMessage(new GameAnnounceMessage("You two have the same cards."));
     /** if they have same hands, no one will be kicked off */
    }
}

