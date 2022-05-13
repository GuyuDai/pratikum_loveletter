package de.swe.oo.server.player;


import de.swe.oo.server.messages.ErrorMessage;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameRequestMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.session.Session;

import java.net.Socket;

public class Player {
    private String name;


    private Session session;
    public Connection connection;
    public ConnectionListener connectionListener;


    private boolean pendingRequestExists;
    private String lastResponse;

    public boolean pendingRequestExists() {
        synchronized (this) {
            return pendingRequestExists;
        }
    }

    public Player(Session session, String name, Socket socket) {
        this.session = session;
        this.name = name;
        this.connection = new Connection(socket);
        this.connectionListener = new ConnectionListener(this);
        this.connectionListener.start();
        String threadName = name.concat("_Listener");
        this.connectionListener.setName(threadName);
        this.pendingRequestExists = false;
        this.lastResponse = "No client responses yet.";
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

    public void quit() {
        session.remove(this);
        connectionListener.close();
        connection.close();
    }

    public boolean requestFromPlayer(GameRequestMessage msg) {
        synchronized (this) {
            if (!pendingRequestExists) {
                sendMessage(msg);
                pendingRequestExists = true;
                return true;
            }
            return false;
        }
    }

    public void processResponse(String response) {
        synchronized (this) {
            if (pendingRequestExists) {
                lastResponse = response;
                pendingRequestExists = false;
                sendMessage(new GameAnnounceMessage("Received an expected input."));
            } else {
                sendMessage(new ErrorMessage("Received an unexpected message."));
            }
        }
    }

    public String getLastResponse() {
        return lastResponse;
    }
}
