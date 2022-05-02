package de.swe.oo.server.session;


import de.swe.oo.server.messages.ChatMessage;
import de.swe.oo.server.player.Player;

import java.util.LinkedList;
import java.util.List;

public class Session {
    //Erstellt eine Liste an Spielern
    List<Player> players;
    LoginHandler loginHandler;
    public Chat chat;

    public Session(int port) {
        this.loginHandler = new LoginHandler(this, port);
        this.chat = new Chat(this);
        this.players = new LinkedList<Player>();
        loginHandler.start(); //Needs to be moved, or does it?
        loginHandler.setName("LoginHandler");
    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public boolean isValidUsername(String testName) {
        boolean valid = true;
        for (Player player : players) {
            valid = valid && (!testName.equals(player.name));
        }
        return valid;
    }

    public void remove(Player player) {
        player.connection.close();
        player.connectionListener.close();
        ChatMessage byeMsg = new ChatMessage(player.name + " left the room.");
        players.remove(player);
        chat.broadcast(byeMsg);
    }

    public static void main(String[] args) {
        Session testsession = new Session(4444);
    }
}
