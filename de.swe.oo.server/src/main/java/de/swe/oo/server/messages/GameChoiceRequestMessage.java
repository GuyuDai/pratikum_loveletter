package de.swe.oo.server.messages;

public class GameChoiceRequestMessage extends GameRequestMessage {
    String[] choices;

    public GameChoiceRequestMessage(String prompt, String[] choices) {
        super(prompt);
        choices = choices;
    }

    @Override
    public String output() {
        return "GAME REQUEST CHOICE " + encodeSpaces(messageText) + choicesString();
    }

    private String choicesString() {
        String result ="";
        for (String choice : choices){
            result = result + " " + encodeSpaces(choice);
        }
        return result;
    }

    private String encodeSpaces(String in) {
        return in.replace(" ", ";");
    }
}
