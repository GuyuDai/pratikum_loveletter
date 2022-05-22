package de.swe.oo.server.cards;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameMessage;
import de.swe.oo.server.player.Player;

public class Prince extends Card {
    private static String NAME = "Prince";
    private static int VALUE = 5;

    public Prince(LoveLetterGame currentGame, Player owner) {
        super("Prince",currentGame);
    }

    // Player may choose any player (including themselves) to discard their hand and draw a new one
    @Override
    public void effect(){

        String [] namelist = currentGame.getNameOfActivePlayers().toArray(new String[0]);
        /** Player can choose a name to show their hands*/
        owner.sendMessage(new GameChoiceRequestMessage("Choose one of the names to discard their deck", namelist));
        /** Chosen player needs to show their cards*/
        Player targetPlayer= currentGame.getPlayer(owner.getLastResponse());
        owner.sendMessage(new GameMessage(currentGame.choosePlayerDeck(targetPlayer)));
        owner.sendMessage(new GameMessage("Do you want to discard their deck?"));
        /** If yes decks gets discarded*/
        if (owner.getLastResponse().equalsIgnoreCase("yes")){
            for (String s : targetPlayer.showHands()) {
                if (s.contains("Princess")){
                    currentGame.removeCurrentPlayer(targetPlayer);
                } else{
                    for (int i = 0; i < targetPlayer.getHandsSize(); i++) {
                        effect(); //TODO: Discard all cards correctly
                        //TODO: Draw cards
                    }
                }
            }
    }
}
}
