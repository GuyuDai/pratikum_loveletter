package de.swe.oo.server.session;

import de.swe.oo.server.game.Game;
import de.swe.oo.server.messages.ChatMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
    }

    public void broadcast(Message msg) {
        for (Player player : players) {
            player.sendMessage(msg);
        }
    }

    public void createGame(){
        currentGame = Game.getGame();
    }

    public boolean joinGame(Player player){

        return currentGame.addPlayer(player);
    }


    public static void main(String[] args) {
        Session testsession = new Session(4444);
        testsession.start();
    }

    public boolean startGame() {
        return currentGame.startGame();
    }
}
