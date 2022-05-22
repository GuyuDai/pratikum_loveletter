package de.swe.oo.server.cards;

import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.player.Player;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {
    private CopyOnWriteArrayList<Card> remainingCards;
    private CopyOnWriteArrayList<Card> usedCards;
    private LoveLetterGame currentGame;

  /**
   * @author dai
   * when the deck is created, the ramaining cards and used cards are all empty at first
   * these will only be initialized after a LoveLetterGame started
   * @param currentGame
   */
  public Deck(LoveLetterGame currentGame) { //create a deck for a round
      this.currentGame = currentGame;
      this.remainingCards = new CopyOnWriteArrayList<Card>();
      this.usedCards = new CopyOnWriteArrayList<Card>();
    }


  public CopyOnWriteArrayList<Card> getRemainingCards() {
    return remainingCards;
  }

  public CopyOnWriteArrayList<Card> getUsedCards() {
    return usedCards;
  }

  /**
   * @author dai
   * get the first card in the remaining cards
   * @return
   */
  public Card getCardFromDeck(){
      return remainingCards.get(0);  //get the first card in the remaining cards
    }

  /**
   * @author dai
   * this method is used to initialize the deck. At the beginning of a round, some cards need to removed out of the game
   */
  public void removeCard(){  //at the beginning of a round, we need to remove some cards out of the round
      Card temp = getCardFromDeck();
      usedCards.add(temp);
      remainingCards.remove(temp);
    }

  /**
   * @author dai
   * @param player is who draws a card
   */
  public void draw(Player player){  //a player draws card
      Card card = getCardFromDeck();  //get a card from the game deck
      card.setOwner(player);  //this card will be assigned to the player who draw card
      player.addHands(card);  //this card will be added to the player's hand
      remainingCards.remove(card);  //remove this card from the deck
    }

  /**
   * @author dai
   * this method is prepared for Prince
   * @param player
   */
  public void drawFromUsedcards(Player player){
      Card card = usedCards.get(0);  //get the first card from the used cards
      card.setOwner(player);
      player.addHands(card);
      usedCards.remove(card);
    }
}
