package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {
    private CopyOnWriteArrayList<Card> remainingCards;
    private CopyOnWriteArrayList<Card> usedCards;

    private LoveLetterGame currentgame;

    public Deck(LoveLetterGame currentgame) { //create a deck for a round
      this.currentgame = currentgame;
      this.remainingCards = new CopyOnWriteArrayList<Card>();
      this.usedCards = new CopyOnWriteArrayList<Card>();
    }


  public CopyOnWriteArrayList<Card> getRemainingCards() {
    return remainingCards;
  }

  public CopyOnWriteArrayList<Card> getUsedCards() {
    return usedCards;
  }

  public Card getCardFromDeck(){
      int parameter = remainingCards.size() + 1;  //because the random() function gives out a floating number x, 0 <= x < 1
      //so we need a parameter to size it up
      int pointer = (int)(Math.random() * parameter);  //0 <= pointer < deck.size() + 1
      return remainingCards.get(pointer);
    }

    public void removeCard(){  //at the beginning of a round, we need to remove some cards out of the round
      Card temp = getCardFromDeck();
      usedCards.add(temp);
      remainingCards.remove(temp);
    }

    public void draw(Player player){  //a player draws card
      Card card = getCardFromDeck();  //get a card from the game deck
      card.setOwner(player);  //this card will be assigned to the player who draw card
      player.setHands(card);  //this card will be added to the player's hand
      remainingCards.remove(card);  //remove this card from the deck
    }

}
