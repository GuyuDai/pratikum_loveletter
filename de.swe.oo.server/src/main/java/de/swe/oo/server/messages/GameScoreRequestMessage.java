package de.swe.oo.server.messages;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.player.Player;

public class GameScoreRequestMessage extends GameMessage{
    public GameScoreRequestMessage() {
        super("");
    }

    @Override
    public void handle(Player player){
        if (player.getSession().isPlaying(player)){
            int score = player.getSession().getCurrentGame().getScore(player);
            player.sendMessage(new GameAnnounceMessage("Your score is " + score + "."));
        } else{
            player.sendMessage(new GameAnnounceMessage("You aren't playing currently."));
        }
    }
}
