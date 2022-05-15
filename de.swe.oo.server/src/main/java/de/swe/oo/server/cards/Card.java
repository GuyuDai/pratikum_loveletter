package de.swe.oo.server.cards;

import de.swe.oo.server.player.Player;
import de.swe.oo.server.game.Game;

public abstract class Card {
    protected String name;
    protected int value;
    protected Game currentGame;
    protected Player owner;


    public Card(String name, int value, Game currentGame, Player owner) {
        this.name = name;
        this.value = value;
        this.currentGame = currentGame;
        this.owner = owner;
    }

    abstract void discard();

    public String getName() {
        return name;
    }
}
