package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class Prince extends Card {
    private static String NAME = "Prince";
    private static int VALUE = 5;

    public Prince(LoveLetterGame currentGame) {
        super("Prince",currentGame);
    }

    // Player may choose any player (including themselves) to discard their hand and draw a new one
    @Override
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        owner.requestFromPlayer((new GameChoiceRequestMessage("Choose one of the names!", namelist)));
        currentGame.waitForAllResponses();
        int responseIndex = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex]);
        //get the target player's hand and check whether it is Princess
        Card targetCard = targetPlayer.getHands(0);
        if(targetCard.getName().equals("Princess")){  //if true, this player discard Princess
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
