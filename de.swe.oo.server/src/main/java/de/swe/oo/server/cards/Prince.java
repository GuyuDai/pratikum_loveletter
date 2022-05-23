package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;
/**
 * @auther Dai, Nik, Minghao
 */
public class Prince extends Card {
    private static String NAME = "PRINCE";
    private static int VALUE = 5;

    public Prince(LoveLetterGame currentGame) {
        super("PRINCE",currentGame);
    }

    // Player may choose any player (including themselves) to discard their hand and draw a new one
    @Override
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        owner.requestFromPlayer((new GameChoiceRequestMessage("Choose one of the players to show their card!", namelist)));
        currentGame.waitForAllResponses();
        int responseIndex = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex]);
        if (targetPlayer==null){
            owner.sendMessage(new GameAnnounceMessage("this player is protected,so your card have no effect"));
            return;
        }
        //get the target player's hand and check whether it is Princess
        Card targetCard = targetPlayer.getHands(0);
        if(targetCard.getName().equals("PRINCESS")){  //if true, this player discard Princess
            targetPlayer.discard(targetCard);
            return;  //this method will end here because who discard Princess will be kicked off
        }
        targetPlayer.discardWithoutEffect(targetPlayer.getHands(0));  //if false, discard this only card without effect
        if(this.currentGame.getDeck().getRemainingCards().size() == 0){  //if there is no remaining card
            this.currentGame.getDeck().drawFromUsedcards(targetPlayer);  //the target player draws a card from the used cards
            return;
        }
        this.currentGame.getDeck().draw(targetPlayer);  //if the remaining cards list is not empty, just draw
    }
}
