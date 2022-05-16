package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.messages.GameInputRequestMessage;
import de.swe.oo.server.player.Player;

import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class LoveLetterGame extends Game {
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;

    public LoveLetterGame() {
        super(MINPLAYERS, MAXPLAYERS);
        switch (this.players.size()){
            case 2:
                this.targetAffection = 7;
                break;
            case 3:
                this.targetAffection = 5;
                break;
            case 4:
                this.targetAffection = 4;
                break;
        }
    }


    protected void handleTurn(Player player) {
        sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + "'s turn."));
        String[] options = {"1", "12", "-1"};
        player.requestFromPlayer(new GameChoiceRequestMessage("Please choose one of the numbers.", options));
        waitForAllResponses();
        int responseIndex = parseInt(player.getLastResponse().trim());
        int response = parseInt(options[responseIndex]);
        changeScore(player, response);
        announceState();
    }

    @Override
    protected void reorderPlayers() {
        HashMap<Player, String> lastDates = new HashMap<>();
        GameInputRequestMessage dateInputRequest = new GameInputRequestMessage(
                "Please enter the date of your last date. (yyyymmdd)");
        for (Player player : players) {
            player.requestFromPlayer(dateInputRequest);
        }
        waitForAllResponses();
    }
}
