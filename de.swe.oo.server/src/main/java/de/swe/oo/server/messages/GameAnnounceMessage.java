package de.swe.oo.server.messages;

/**
 * @author Franz
 */
public class GameAnnounceMessage extends GameMessage{
    public GameAnnounceMessage(String messageText) {
        super(messageText);
    }

    @Override
    public String output(){
        return "GAME ANNOUNCE " + messageText;
    }
}
