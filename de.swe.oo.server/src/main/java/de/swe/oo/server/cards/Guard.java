package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;


import static java.lang.Integer.parseInt;

public class Guard extends Card{
    private static String NAME = "Guard";
    private static int VALUE = 1;

    public Guard(LoveLetterGame currentGame) {
        super("Guard",currentGame);
    }

    @Override
    /**When you discard the Guard, choose a player and name a number (other than 1).
     * If that player has that number in their hand, that player is knocked out of the round.
     * @author Dai, Minghao, Nassrin, Niklas, Nargess
     */
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name */
        owner.requestFromPlayer((new GameChoiceRequestMessage("Choose one of the names!", namelist)));
        currentGame.waitForAllResponses();
        int responseIndex1 = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex1]);
        /** Choose number value of cards*/
        String[] options = {"2", "3", "4", "5", "6", "7", "8"};
        owner.requestFromPlayer((new GameChoiceRequestMessage("Choose one of the numbers!", options)));
        currentGame.waitForAllResponses();
        //owner.sendMessage(new GameChoiceRequestMessage("Choose number", options));
        int responseIndex2 = parseInt(owner.getLastResponse().trim());
        int response = parseInt(options[responseIndex2]);
        Card targetPlayerCardOne= targetPlayer.getHands(0);

        /**If number is consistent with value of one of the cards eliminate player*/

        if(response == targetPlayerCardOne.getValue() && targetPlayer.getHandsSize() == 1){
            currentGame.playerKickedOff(targetPlayer);
            return;
        }
        if(targetPlayer.getHandsSize() == 2){
            Card targetPlayerCardTwo= targetPlayer.getHands(1);
            if(response == targetPlayerCardTwo.getValue()){
                currentGame.playerKickedOff(targetPlayer);
                return;
            }
        }

    }
}
