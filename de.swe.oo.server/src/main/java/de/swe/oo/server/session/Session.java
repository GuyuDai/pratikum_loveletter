package de.swe.oo.server.session;

import de.swe.oo.server.messages.ChatMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Session extends Thread {
    public Chat chat;
    List<Player> players;
    LoginHandler loginHandler;

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
            valid = valid && (!testName.equals(player.name));
        }
        return valid;
    }

    public void remove(Player player) {
        ChatMessage byeMsg = new ChatMessage(player.name + " left the room.");
        players.remove(player);
        broadcast(byeMsg);
    }

    public void broadcast(Message msg) {
        for (Player player : players) {
            player.sendMessage(msg);
        }
    }

    public static void main(String[] args) {
        Session testsession = new Session(4444);
        testsession.start();
    }
}
