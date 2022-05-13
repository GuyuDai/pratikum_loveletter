package de.swe.oo.server.game;

import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game extends Thread {
    private static int BUSYWAIT_INTERVALMS = 40;
    protected int minPlayers;
    protected int maxPlayers;

    protected boolean isGoingOn;
    protected CopyOnWriteArrayList<Player> players;
    protected HashMap<Player, Integer> scoreMap;

    public Game(int minPlayers, int maxPlayers) {
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        players = new CopyOnWriteArrayList<Player>();
        scoreMap = new HashMap<Player, Integer>();
        isGoingOn = false;
    }

    public boolean isGoingOn() {
        return isGoingOn;
    }

    @Override
    public void run() {
        sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
        reorderPlayers();
        initializeScores();
        while (isGoingOn) {
            for (Player player : players) {
                handleTurn(player);
                if (!isGoingOn) {
                    break;     //Otherwise all players will have a turn after shutdown was initialized.
                }
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

    protected void cleanUp() {
        sendToAllPlayers(new GameAnnounceMessage("Shutting down the game. Final scores: " + getScoreString()));
    }


    public synchronized boolean join(Player player) {
        if (players.size() < this.maxPlayers && !isGoingOn && !players.contains(player)) {
            players.add(player);
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean startGame() {
        if (players.size() >= this.minPlayers) {
            isGoingOn = true;
            this.start();
            this.setName("GameThread");
            return true;
        }
        return false;
    }

    /**
     * Currently this only serves as a demonstration. It just ouputs the order of the players. Overwrite this method
     * in a subclass if you want to manually order the players.
     */
    protected void reorderPlayers() {
        int i = 1;
        for (Player player : players) {
            sendToAllPlayers(new GameAnnounceMessage(player.getName() + " is player number " + i + "."));
            ++i;
        }
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


    public void shutdown() {
        isGoingOn = false;
    }

    public int getScore(Player player) {
        return scoreMap.get(player);
    }
    protected void changeScore(Player player, int change){
        int oldScore = scoreMap.get(player);
        scoreMap.put(player, oldScore + change);
    }
    protected String getScoreString() {
        String scoreString = "";
        for (Player player : players) {
            scoreString = scoreString + player.getName() + ": " + scoreMap.get(player) + " ";
        }
        return scoreString;
    }

    protected void waitForAllResponses() {
        boolean requestsOngoing = areRequestsOngoing();
        while (isGoingOn && requestsOngoing) {
            try {
                sleep(BUSYWAIT_INTERVALMS);
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for responses from players. " + e.getMessage());
            }
            requestsOngoing = areRequestsOngoing();
        }
    }

    private boolean areRequestsOngoing() {
        boolean requestsOngoing = false;
        for (Player player : players) {
            if (player.pendingRequestExists()) {
                requestsOngoing = true;
                break;
            }
        }
        return requestsOngoing;
    }

    protected void announceState(){
        sendToAllPlayers(new GameAnnounceMessage("Current Scores: " + getScoreString()));
    }

}
