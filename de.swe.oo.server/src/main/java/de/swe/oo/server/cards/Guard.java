package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class Guard extends Card{
    private static String NAME = "Guard";
    private static int VALUE = 1;

    public Guard(LoveLetterGame currentGame, Player owner) {
        super(NAME, VALUE, currentGame, owner);
    }

    @Override
    /**When you discard the Guard, choose a player and name a number (other than 1).
     * If that player has that number in their hand, that player is knocked out of the round.
     */
    void discard(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name */
        owner.sendMessage(new GameChoiceRequestMessage("Choose one of the names", namelist));
        Player targetPlayer= currentGame.getPlayer(owner.getLastResponse());
        /** Choose number value of cards*/
        String[] options = {"2", "3", "4", "5", "6", "7", "8"};
        owner.sendMessage(new GameChoiceRequestMessage("Choose number", options));
        int responseIndex = parseInt(owner.getLastResponse().trim());
        int response = parseInt(options[responseIndex]);
        Card targetPlayerCardOne= targetPlayer.getHands(0);
        Card targetPlayerCardTwo= targetPlayer.getHands(1);

        /**If number is consistent with value of one of the cards eliminate player*/

        if(response == targetPlayerCardOne.value || response == targetPlayerCardTwo.value){
            currentGame.playerKickedOff(targetPlayer);
        }

    }
}
