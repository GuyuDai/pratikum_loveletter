package de.swe.oo.server.game;

import de.swe.oo.server.player.Player;

import java.util.ArrayList;

public class Game extends Thread{
    protected static final int MINPLAYERS = 2;
    protected static final int MAXPLAYERS = 4;
    private static volatile Game gameInstance;


    private boolean isGoingOn;
    private ArrayList<Player> players;
    public Game(){
        players = new ArrayList<Player>();
        isGoingOn = false;
    }
    public synchronized boolean isGoingOn() {
        return isGoingOn;
    }

    @Override
    public void run(){
        while(isGoingOn){
            try{
                sleep(2000);
                System.out.println("Game is going on.");
            } catch(InterruptedException e){
                System.err.println("An error occurred during sleep. " + e.getMessage());
            }
        }
    }

    public synchronized boolean addPlayer(Player player) {
        if (players.size() < MAXPLAYERS){
            players.add(player);
            return true;
        }
        else{
            return false;
        }
    }

    public synchronized boolean startGame() {
        if (players.size() >= MINPLAYERS){
            isGoingOn = true;
            this.start();
            return true;
        }
        return false;
    }
}
