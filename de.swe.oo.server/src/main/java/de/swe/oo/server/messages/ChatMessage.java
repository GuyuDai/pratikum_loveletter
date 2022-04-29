package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

public class ChatMessage extends Message {

    public ChatMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        if (messageText.equals("bye")) {
            player.session.remove(player);
            return;
        }
        ChatMessage messageWithSender =
                new ChatMessage(player.name + ": " + messageText);
        player.session.chat.broadcast(messageWithSender);
    }

    public String output() {
        return "CHAT " + messageText;
    }
}
