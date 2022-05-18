package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {
    private CopyOnWriteArrayList<Card> deck;
    private CopyOnWriteArrayList<Card> usedCards;

    private LoveLetterGame currentgame;
    private Player owner;

    public Deck(LoveLetterGame currentgame) {
      this.currentgame = currentgame;
      this.deck = new CopyOnWriteArrayList<Card>();
      this.usedCards = new CopyOnWriteArrayList<Card>();
    }

    public Deck(Player owner){
      this.owner = owner;
      this.deck = new CopyOnWriteArrayList<Card>();
    }

    public void addCard(Card.CardType type) {
      Card card = new Card(type);
      deck.add(card);
    }

    public Card getCardFromDeck(){
      int parameter = deck.size() + 1;  //because the random() function gives out a floating number x, 0 <= x < 1
      //so we need a parameter to size it up
      int pointer = (int)(Math.random() * parameter);  //0 <= pointer < deck.size() + 1
      return deck.get(pointer);
    }

    public void removeCard(){  //at the beginning of a round, we need to remove some cards out of the round
      Card temp = getCardFromDeck();
      usedCards.add(temp);
      deck.remove(temp);
    }

    public void draw(Player player){  //a player draws card
      Card temp = getCardFromDeck();
      player.setHands(temp);
      deck.remove(temp);
    }
}
