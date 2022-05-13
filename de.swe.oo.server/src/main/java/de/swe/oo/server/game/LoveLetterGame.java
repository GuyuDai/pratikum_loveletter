package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameInputRequestMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.HashMap;

public class LoveLetterGame extends Game{
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;
    public LoveLetterGame(){
        super(MINPLAYERS, MAXPLAYERS);
    }


    protected void handleTurn(Player player){
        try {
            sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + "'s turn."));
            sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void reorderPlayers(){
        HashMap<Player, String> lastDates = new HashMap<>();
        GameInputRequestMessage dateInputRequest = new GameInputRequestMessage(
                "Please enter the date of your last date. (yyyymmdd)");
        for (Player player : players){
            player.requestFromPlayer(dateInputRequest);
        }
        waitForAllResponses();
    }
}
