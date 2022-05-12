package de.swe.oo.server.messages;

import de.swe.oo.server.player.Player;

public class GameStatusRequestMessage extends GameMessage {
    public GameStatusRequestMessage() {
        super("");
    }

    @Override
    public void handle(Player player) {
        if (!player.getSession().gameExists()) {
            player.sendMessage(new GameAnnounceMessage("Currently no game exists."));
        } else {
            int nPlayers = player.getSession().getCurrentGame().getNumberOfPlayers();
            boolean isGoingOn = player.getSession().getCurrentGame().isGoingOn();
            String possibleNegation = "";
            if (!isGoingOn) {
                possibleNegation = "not ";
            }
            player.sendMessage(new GameAnnounceMessage("Currently there are " + nPlayers +
                    " players in the Game and it is " + possibleNegation + "running."));
        }
    }
}
