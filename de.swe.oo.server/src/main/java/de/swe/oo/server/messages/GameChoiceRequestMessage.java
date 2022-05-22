package de.swe.oo.server.messages;

public class GameChoiceRequestMessage extends GameRequestMessage {
    String [] choices;

    /**
     * Requests a choice from a player. The player is supposed to send back an index into the array. Might also be
     * abused to present information to the player by telling them to choose anything to continue.
     * @param prompt The text that is displayed to the player when they are supposed to make the choice.
     *               For example: "Choose one of the following cards:".
     * @param choices An array of Strings with the actual possible choices,
     *                for example {"Choice 1", "Choice 2", "Choice 3"}.
     */
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
        for (int i = 0; i< choices.length; ++i){
            result = result + " " + encodeSpaces(choices[i]);
        }
        return result;
    }

    private String encodeSpaces(String in) {
        return in.replace(" ", ";");
    }
}
