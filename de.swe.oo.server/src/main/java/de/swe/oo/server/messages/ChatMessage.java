package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

/**
 * The chat messages is one of the few messages that flow in both directions. This might be confusing and could
 * probably changed by splitting it into a InChatMessage and OutChatMessage.
 */
public class ChatMessage extends Message {

    public ChatMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        if (messageText.equals("bye")) {
            player.quit();
            return;
        }

        if(messageText.startsWith("@")){
            //if the @ is inside the message, this message will be regarded as a normal ChatMessage
            String targetName = messageText.substring(1, messageText.indexOf(" "));
            String targetMessage = messageText.substring(messageText.indexOf(" "));
            Player target = player.getSession().getPlayerByName(targetName);
            if(target != null){
                //@person in the game
                target.sendMessage(new ChatMessage
                    ("[" + player.getName() + "]" + targetMessage));
                player.sendMessage(new ChatMessage
                    (player.getName() + "->" + target.getName() + targetMessage));
            }else{
                //@person not in the game
                player.sendMessage(new ChatMessage
                    (" could not send private message, unknown player name"));
            }
        }else{
            //is not private message
            player.getSession().broadcast(new ChatMessage(player.getName() + ":" + ""+ messageText));
        }
    }

    public String output() {
        return "CHAT " + messageText;
    }
}
