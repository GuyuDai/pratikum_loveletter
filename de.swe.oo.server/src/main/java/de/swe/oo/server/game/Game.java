package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Thread {
    protected static final int MINPLAYERS = 2;
    protected static final int MAXPLAYERS = 4;
    private static volatile Game gameInstance;


    private boolean isGoingOn;
    private CopyOnWriteArrayList<Player> players;

    public Game() {
        players = new CopyOnWriteArrayList<Player>();
        isGoingOn = false;
    }

    public boolean isGoingOn() {
        return isGoingOn;
    }

    @Override
    public void run() {
        while (isGoingOn) {
            try {
                sleep(2000);
                System.out.println("Game is going on.");
                sendAllPlayers(new GameAnnounceMessage("The Game is going along fine."));
            } catch (InterruptedException e) {
                System.err.println("An error occurred during sleep. " + e.getMessage());
            }
        }
    }

    public synchronized boolean join(Player player) {
        if (players.size() < MAXPLAYERS && !isGoingOn) {
            players.add(player);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean startGame() {
        if (players.size() >= MINPLAYERS) {
            isGoingOn = true;
            this.start();
            return true;
        }
        return false;
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    private void sendAllPlayers(Message msg) {
        for (Player player : players) {
            player.sendMessage(msg);
        }
    }
}
