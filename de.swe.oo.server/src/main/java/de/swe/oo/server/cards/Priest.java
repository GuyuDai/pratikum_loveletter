package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

import static java.lang.Integer.parseInt;

public class Priest extends Card {
    private static String NAME = "Priest";
    private static int VALUE = 2;

    public Priest(LoveLetterGame currentGame, Player owner) {
        super("Priest", currentGame);
    }

    //Player may privately see another players hand
    @Override
    public void effect(){
        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.sendMessage(new GameChoiceRequestMessage("Choose one of the names to look into their deck", namelist));
        /** Chosen player needs to show their cards*/
        int responseindex= parseInt(owner.getLastResponse().trim());
        Player targetPlayer= currentGame.getPlayer(namelist[responseindex]);
            owner.sendMessage(new GameAnnounceMessage("the first card is "+ targetPlayer.getHands(0).getName()));
            owner.sendMessage(new GameAnnounceMessage("the second card is "+ targetPlayer.getHands(2).getName()));
            //TODO: Private Message to the owner and outprint card1 and card2

    }
}
