package de.swe.oo.server.player;


import de.swe.oo.server.messages.Message;
import de.swe.oo.server.session.Session;

import java.net.Socket;

public class Player {
    private String name;


    private Session session;
    public Connection connection;
    public ConnectionListener connectionListener;

    public Player(Session session, String name, Socket socket) {
        this.session = session;
        this.name = name;
        this.connection = new Connection(socket);
        this.connectionListener = new ConnectionListener(this);
        this.connectionListener.start();
        String threadName = name.concat("_Listener");
        this.connectionListener.setName(threadName);
    }
    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }

    public void sendMessage(Message msg) {
        connection.sendLine(msg.output());
    }

    public void quit(){
        session.remove(this);
        connectionListener.close();
        connection.close();
    }
}
