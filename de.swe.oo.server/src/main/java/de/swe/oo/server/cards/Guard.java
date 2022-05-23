package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;


import static java.lang.Integer.parseInt;

public class Guard extends Card{
    private static String NAME = "GUARD";
    private static int VALUE = 1;

    public Guard(LoveLetterGame currentGame) {
        super("GUARD",currentGame);
    }

    @Override
    /**When you discard the Guard, choose a player and name a number (other than 1).
     * If that player has that number in their hand, that player is knocked out of the round.
     * @author Dai, Minghao, Nassrin, Niklas, Nargess
     */
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name */
        owner.requestFromPlayer((new GameChoiceRequestMessage("Choose one of the players!", namelist)));
        currentGame.waitForAllResponses();
        int responseIndex1 = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex1]);
        if (targetPlayer==null){
            owner.sendMessage(new GameAnnounceMessage("this player is protected,so your card have no effect"));
            return;
        }
        /** Choose number value of cards*/
        String[] options = {"2", "3", "4", "5", "6", "7", "8"};
        owner.requestFromPlayer((new GameChoiceRequestMessage("Now choose a number!", options)));
        currentGame.waitForAllResponses();
        //owner.sendMessage(new GameChoiceRequestMessage("Choose number", options));
        int responseIndex2 = parseInt(owner.getLastResponse().trim());
        int response = parseInt(options[responseIndex2]);
        Card targetPlayerCardOne= targetPlayer.getHands(0);
        /** each player except owner can only have one card when it is s not his/her turn*/
        /**If number is consistent with value of one of the cards eliminate player*/

        if(response == targetPlayerCardOne.getValue()){
            currentGame.playerKickedOff(targetPlayer);
            return;
        }
       else owner.sendMessage(new GameAnnounceMessage("Your guess was wrong! "));
    }
}
