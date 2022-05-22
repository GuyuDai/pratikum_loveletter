package de.swe.oo.server.session;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.ChatMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main Server class. Contains references to pretty much all state of the server application and handles setup and
 * shutdown.
 */
public class Session extends Thread {
    public Chat chat;
    List<Player> players;
    
    LoginHandler loginHandler;
    Game currentGame;

    public Session(int port) {
        this.loginHandler = new LoginHandler(this, port);
        this.chat = new Chat(this);
        this.players = new CopyOnWriteArrayList<Player>();
    }
    public Game getCurrentGame() {
        return currentGame;
    }


    @Override
    public void run() {
        loginHandler.start();
        loginHandler.setName("LoginHandler");
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public boolean checkIfValidUsername(String testName) {
        boolean valid = true;
        for (Player player : players) {
            valid = valid && (!testName.equals(player.getName()));
        }
        return valid;
    }

    public void remove(Player player) {
        ChatMessage byeMsg = new ChatMessage(player.getName() + " left the room.");
        players.remove(player);
        broadcast(byeMsg);
        if (isPlaying(player)){
            if (currentGame != null){
                currentGame.shutdown();
            }
            currentGame = null;
        }
    }

    public void broadcast(Message msg) {
        for (Player player : players) {
            player.sendMessage(msg);
        }
    }

    public void sendTo(Message msg, String targetName){
        getPlayerByName(targetName).sendMessage(msg);
    }

    public List<String> getPlayers(){
        List<String> players_name = new ArrayList<>();
        for(Player player : players){
            players_name.add(player.getName());
        }
        return players_name;
    }

    public Player getPlayerByName(String targetName){
        for(Player player : players){
            if(targetName.equals(player.getName())){
                return player;
            }
        }
        return null;
    }

    public synchronized void createNewGame() {
        currentGame = new LoveLetterGame();
    }

    public boolean joinGame(Player player) {
        return currentGame.join(player);
    }


    public static void main(String[] args) {
        Session testsession = new Session(4444);
        testsession.start();
    }

    public boolean startGame() {
        if (gameExists()) {
            return currentGame.startGame();
        }
        else{
            return false;
        }
    }

    public boolean gameExists() {
        return currentGame != null;
    }

    public boolean isPlaying(Player player) {
        return currentGame == null || currentGame.isPlaying(player);
    }
}
