package de.swe.oo.server.messages;

import de.swe.oo.server.player.Player;

/**
 * Sent by a client to entirely leave a session.
 */
public class ExitMessage extends Message {
    @Override
    public void handle(Player player) {
        player.quit();
    }

    @Override
    public String output() {
        return null;
    }
}
