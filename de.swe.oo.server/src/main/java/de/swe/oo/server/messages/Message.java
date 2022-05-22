package de.swe.oo.server.messages;


import de.swe.oo.server.player.Player;

/**
 * A message needs to implement two methods: Handle that specifies what should be done if a Message of this type is
 * received and output, that describes the sort of protocol via which the message is sent. Actually it might be possible
 * and desirable to split the Message class into an InMessage and OutMessage class, that implement only handle and output
 * respectively.
 * This might be advantageous as currently only the ChatMessage actually needs both methods as it can be sent in both
 * directions. (server -> client, client -> server)
 */
abstract public class Message {
    String messageText;

    abstract public void handle(Player player);

    abstract public String output();
}
