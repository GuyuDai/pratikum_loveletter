package de.swe.oo.server.messages;

public class GameInputRequestMessage extends GameRequestMessage {

    public GameInputRequestMessage(String messageText) {
        super(messageText);
    }

    @Override
    public String output(){
        return "GAME REQUEST INPUT " + messageText;
    }
}
