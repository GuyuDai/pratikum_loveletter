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
                this.value = 1;
                break;

            case "Priest":
                this.value = 2;
                break;

            case "Baron":
                this.value = 3;
                break;

            case "Handmaid":
                this.value = 4;
                break;

            case "Prince":
                this.value = 5;
                break;

            case "King":
                this.value = 6;
                break;

            case "Countess":
                this.value = 7;
                break;

            case "Princess":
                this.value = 8;
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
}
