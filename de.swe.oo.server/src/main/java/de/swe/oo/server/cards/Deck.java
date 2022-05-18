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

    public void removeCard(){  //at the beginning of a round, we need to remove some cards out of the round
      int parameter = deck.size() - 1;  //
    }
}
