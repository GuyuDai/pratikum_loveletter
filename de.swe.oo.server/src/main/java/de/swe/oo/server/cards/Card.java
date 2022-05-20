package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;

public abstract class Card {

    protected String name;
    protected int value;
    protected LoveLetterGame currentGame;
    protected Player owner;


    public Card(String name, LoveLetterGame currentGame){
        this.currentGame = currentGame;
        switch (name){
            case "Guard" :
                this.value = "Guard";
                break;

            case "Priest":
                this.value = 2;
                this.name = "Priest";
                break;

            case "Baron":
                this.value = 3;
                this.name = "Baron";
                break;

            case "Handmaid":
                this.value = 4;
                this.name = "Handmaid";
                break;

            case "Prince":
                this.value = 5;
                this.name = "Prince";
                break;

            case "King":
                this.value = 6;
                this.name = "King";
                break;

            case "Countess":
                this.value = 7;
                this.name = "Countess";
                break;

            case "Princess":
                this.value = 8;
                this.name = "Princess";
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
