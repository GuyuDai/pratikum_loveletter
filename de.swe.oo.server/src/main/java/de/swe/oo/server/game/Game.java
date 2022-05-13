package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Thread {
    protected int MINPLAYERS;
    protected int MAXPLAYERS;
    private static volatile Game gameInstance;


    private boolean isGoingOn;
    private CopyOnWriteArrayList<Player> players;
    private HashMap<Player, Integer> scoreMap;

    public Game(int minPlayers, int maxPlayers) {
        players = new CopyOnWriteArrayList<Player>();
        scoreMap = new HashMap<Player, Integer>();
        isGoingOn = false;
        MINPLAYERS = minPlayers;
        MAXPLAYERS = maxPlayers;
    }

    public boolean isGoingOn() {
        return isGoingOn;
    }

    @Override
    public void run() {
        while (isGoingOn) {
            for (Player player : players) {
                handleTurn(player);
            }
        }
        cleanUp();
    }

    protected void handleTurn(Player player) {
        sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + "'s turn."));
        scoreMap.put(player, scoreMap.get(player) + 1);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            System.err.println("Error while sleeping in Game Thread. " + e.getMessage());
        }
    }

    protected void cleanUp(){
        sendToAllPlayers(new GameAnnounceMessage("Shutting down the game. Final scores: " + getScoreString()));
    }


    public synchronized boolean join(Player player) {
        if (players.size() < this.MAXPLAYERS && !isGoingOn && !players.contains(player)) {
            players.add(player);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean startGame() {
        if (players.size() >= this.MINPLAYERS) {
            reorderPlayers();
            isGoingOn = true;
            this.start();
            sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
            initializeScores();
            return true;
        }
        return false;
    }

    protected void reorderPlayers() {
        return;     //Currently the players stay in the order they joined the game.
    }

    protected void initializeScores() {
        for (Player player : players) {
            scoreMap.put(player, 0);
        }
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    protected void sendToAllPlayers(Message msg) {
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

    public void shutdown() {
        isGoingOn = false;
    }

    protected String getScoreString() {
        String scoreString = "";
        for (Player player : players) {
            scoreString = scoreString + player.getName() + ": " + scoreMap.get(player) + " ";
        }
        return scoreString;
    }
}
