package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

public class ChatMessage extends Message {

    public ChatMessage(String messageText) {
        this.messageText = messageText;
    }

    public void handle(Player player) {
        if (messageText.equals("bye")) {
            player.quit();
            return;
        }

        if(messageText.contains("@")){
            int begin = messageText.lastIndexOf("@") + 1;
            int end = messageText.length() - 1;
            while(begin != end){
                String name = messageText.subSequence(begin, end).toString();
                for(String player_name : player.session.getPlayers()){
                    if(name.equals(player_name)){
                        ChatMessage messageWithSender =
                            new ChatMessage(player.name + ": " + messageText);
                        player.session.sendTo(messageWithSender, name);
                        return;
                    }
                }
                end --;
            }
            player.session.sendTo
                (new ChatMessage("the @person is not in the game"), player.name);

        }else{
            ChatMessage messageWithSender =
            new ChatMessage(player.name + ": " + messageText);
            player.session.broadcast(messageWithSender);
        }
    }

    public String output() {
        return "CHAT " + messageText;
    }
}
