package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;

/**
 * @auther Dai, Nik, Nargess, Minghao, Nassrin
 */
public abstract class Card {

    protected String name;
    protected int value;
    protected LoveLetterGame currentGame;
    protected Player owner;


    public Card(String name, LoveLetterGame currentGame){
        this.currentGame = currentGame;
        switch (name){
            case "GUARD" :
                this.value = 1;
                this.name="GUARD";
                break;

            case "PRIEST":
                this.value = 2;
                this.name="PRIEST";
                break;

            case "BARON":
                this.value = 3;
                this.name="BARON";
                break;

            case "HANDMAID":
                this.value = 4;
                this.name="HANDMAID";
                break;

            case "PRINCE":
                this.value = 5;
                this.name="PRINCE";
                break;

            case "KING":
                this.value = 6;
                this.name="KING";
                break;

            case "COUNTESS":
                this.value = 7;
                this.name="COUNTESS";
                break;

            case "PRINCESS":
                this.value = 8;
                this.name="PRINCESS";
                break;
        }
    }

    public abstract void effect();

    public String getName() {
        return name;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getValue() {
        return value;
    }
}
