package de.swe.oo.server.game;

import de.swe.oo.server.player.Player;

public class LoveLetterGame extends Game{
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;
    public LoveLetterGame(){
        super(MINPLAYERS, MAXPLAYERS);
    }
    @Override
    protected void handleTurn(Player player){
        try {
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void reorderPlayers(){
        return;
    }
}
