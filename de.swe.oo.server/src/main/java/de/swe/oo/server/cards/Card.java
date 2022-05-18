package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;
import de.swe.oo.server.game.Game;

public abstract class Card {
    public static enum CardType {
        GUARD,
        PRIEST,
        BARON,
        HANDMAID,
        PRINCE,
        KING,
        COUNTESS,
        PRINCESS
    }

    protected String name;
    protected CardType type;
    protected final int value;

    protected LoveLetterGame currentGame;
    protected Player owner;


    public Card(CardType type, String name, int value, LoveLetterGame currentGame, Player owner) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.currentGame = currentGame;
        this.owner = owner;
    }

    public Card(CardType type, LoveLetterGame currentGame){
        this.type = type;
        this.currentGame = currentGame;
        switch (type){
            case GUARD :
                this.name = "gurad";
                this.value = 1;
                break;

            case PRIEST:

        }
    }

    abstract void discard();

    public String getName() {
        return name;
    }
}
