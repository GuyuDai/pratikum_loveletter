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
        ChatMessage messageWithSender =
                new ChatMessage(player.getName() + ": " + messageText);
        player.getSession().broadcast(messageWithSender);
    }

    public String output() {
        return "CHAT " + messageText;
    }
}
