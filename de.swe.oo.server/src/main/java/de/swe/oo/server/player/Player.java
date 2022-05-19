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

    /**
     * Owner: Franz Bauernschmitt
     * @param session
     * @param name Player Name.
     * @param socket The socket that belongs to the connecting client.
     */
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

    /**
     *
     * This method takes in an object that inherits from the Message class and thus has an output method. It sends
     * the results of this method to the player.
     * @param msg message to be sent to the client.
     */
    public void sendMessage(Message msg) {
        connection.sendLine(msg.output());
    }

    /**
     * Removes this player from its current session and closes the connection. Leaving potentially running games is
     * handled by the session.remove method and thus doesn't have to be handled here.
     */
    public void quit() {
        session.remove(this);
        connectionListener.close();
        connection.close();
    }

    /**
     *  Only one request per player can be pending at one time. This is enforced by this method by returning false
     *  if there is already a message. It might be a better choice to throw a custom error if there already is one,
     *  as the return value of this functions seems like it isn't actually checked in most code.
     * @param msg A GameRequestMessage to be sent to the player. Currently either choice or input request.
     * @return
     */
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

    /**
     * All raw messages arriving at the connectionListener starting with RESPONSE are sent here for further processing.
     * Here it is checked, if a response is actually expected currently in case the player sends a response despite
     * no request.
     * @param response a String that was received by the ConnectionListener of the player.
     */
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
