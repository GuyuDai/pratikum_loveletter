package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Thread {
    protected static final int MINPLAYERS = 2;
    protected static final int MAXPLAYERS = 4;
    private static volatile Game gameInstance;


    private boolean isGoingOn;
    private CopyOnWriteArrayList<Player> players;
    private HashMap<Player, Integer> scoreMap;

    public Game() {
        players = new CopyOnWriteArrayList<Player>();
        scoreMap = new HashMap<Player, Integer>();
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
                sendToAllPlayers(new GameAnnounceMessage("The Game is going along fine."));
            } catch (InterruptedException e) {
                System.err.println("An error occurred during sleep. " + e.getMessage());
            }
        }
    }

    public synchronized boolean join(Player player) {
        if (players.size() < MAXPLAYERS && !isGoingOn && !players.contains(player)) {
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
            sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
            initializeScores();
            return true;
        }
        return false;
    }

    private void initializeScores() {
        for (Player player : players) {
            scoreMap.put(player, 0);
        }
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    private void sendToAllPlayers(Message msg) {
        for (Player player : players) {
            player.sendMessage(msg);
        }
    }

    public boolean isPlaying(Player player) {
        return players.contains(player);
    }

    public int getScore(Player player) {
        return scoreMap.get(player);
    }

    public synchronized void shutdown() {
        if (isGoingOn) {
            sendToAllPlayers(new GameAnnounceMessage("Shutting down the game. Final scores: " + getScoreString()));
            isGoingOn = false;
        }
    }
    private String getScoreString(){
        String scoreString = "";
        for (Player player : players){
            scoreString = scoreString + player.getName() + ": " + scoreMap.get(player) + " ";
        }
        return scoreString;
    }
}
