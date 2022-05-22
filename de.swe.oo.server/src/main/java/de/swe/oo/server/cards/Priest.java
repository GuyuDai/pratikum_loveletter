package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

/**
 * @auther Dai, Nik, Nargess, Nassrin, Minghao
 */
public class Priest extends Card {
    private static String NAME = "PRIEST";
    private static int VALUE = 2;

    public Priest(LoveLetterGame currentGame) {
        super("PRIEST", currentGame);
    }

    /**Player may privately see another players hand*/
    @Override
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.requestFromPlayer(new GameChoiceRequestMessage("Choose one of the players to look into their deck", namelist));
        currentGame.waitForAllResponses();
        /** Chosen player needs to show his/her cards*/
        int responseIndex1 = parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseIndex1]);
           Card card1 = targetPlayer.getHands(0);
        /** each player except owner can only have one card when it is s not his/her turn*/
        owner.sendMessage(new GameAnnounceMessage(targetPlayer.getName()+"'s card is "+card1.getName()));

    }
}

