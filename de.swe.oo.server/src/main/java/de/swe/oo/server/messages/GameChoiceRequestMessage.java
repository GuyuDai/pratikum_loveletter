package de.swe.oo.server.messages;

public class GameChoiceRequestMessage extends GameRequestMessage {
    String[] choices;

    public GameChoiceRequestMessage(String prompt, String[] choices) {
        super(prompt);
        this.choices = choices;
    }

    @Override
    public String output() {
        return "GAME REQUEST CHOICE " + encodeSpaces(messageText) + choicesString();
    }

    private String choicesString() {
        String result ="";
        for (int i=0; i< choices.length; ++i){
            result = result + " " + encodeSpaces(choices[i]);
        }
        return result;
    }

    private String encodeSpaces(String in) {
        return in.replace(" ", ";");
    }
}
