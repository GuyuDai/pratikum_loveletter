package de.swe.oo.server.player;


import de.swe.oo.server.messages.Message;
import de.swe.oo.server.session.Session;

public class Player {
    public String name;
    public Session session;
    public Connection connection;
    public ServerListener serverListener;

    public Player(Session session, String name, int port) {
        this.session = session;
        this.name = name;
        this.connection = new Connection(port);
        this.connection.start();
        this.serverListener = new ServerListener(this);
        this.serverListener.start();
        String threadName = name.concat("_Listener");
        this.serverListener.setName(threadName);
    }

    public void sendMessage(Message msg) {
        connection.sendLine(msg.output());
    }

}