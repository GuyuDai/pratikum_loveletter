package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class Baron extends Card {
    private static String NAME = "Baron";
    private static int VALUE = 3;

    public Baron(LoveLetterGame currentGame) {
        super("Baron", currentGame);
    }

    /**Player may choose another player and privately compare hands. The player with the lower-value card is eliminated.
     * @Author Nik*/
    @Override
    public void effect(){
    /** create nameList with all the Names of active Players */
    String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
    /** Player can choose a name to show their hands*/
    owner.requestFromPlayer(new GameChoiceRequestMessage("Choose one of the names to compare deck", namelist));
    currentGame.waitForAllResponses();
    /** Chosen player needs to show his/her cards*/
    int responseIndex1 = parseInt(owner.getLastResponse().trim());
    Player targetPlayer= currentGame.getPlayer(namelist[responseIndex1]);
    owner.sendMessage(new GameMessage(currentGame.choosePlayerDeck(targetPlayer)));
    /** compare hands and eliminate player with lower hand*/
    Card targetPlayerCardOne= targetPlayer.getHands(0);
    Card targetPlayerCardTwo= targetPlayer.getHands(1);
    Card ownerCardOne= owner.getHands(0);
    Card ownerCardTwo= owner.getHands(1);
    /** The player with the smaller hand looses */
    int ownerCardsAddedValue = ownerCardOne.value + ownerCardTwo.value;
    int targetPlayerCardsAddedValue= targetPlayerCardOne.value + targetPlayerCardTwo.value;
    if (ownerCardsAddedValue<targetPlayerCardsAddedValue){
        currentGame.playerKickedOff(owner);
    } else   currentGame.playerKickedOff(targetPlayer);
    }
}

