package de.swe.oo.server.messages;


/**
 * @author Franz
 * Superclass of all RequestMessages that can be sent to a player via the requestFromPlayer method of the player class.
 */
public class GameRequestMessage extends GameMessage{
    public GameRequestMessage(String messageText) {
        super(messageText);
    }
}
