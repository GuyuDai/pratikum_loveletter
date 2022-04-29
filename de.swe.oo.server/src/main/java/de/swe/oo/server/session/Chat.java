package de.swe.oo.server.session;


import de.swe.oo.server.messages.Message;
import de.swe.oo.server.player.Player;

public class Chat {
    Session session;

    public Chat(Session session) {
        this.session = session;
    }

    public void broadcast(Message msg) {
        for (Player player : session.players) {
            player.sendMessage(msg);
        }
    }
}
