package de.swe.oo.server.game;

import de.swe.oo.server.cards.Card;
import de.swe.oo.server.cards.Deck;
import de.swe.oo.server.cards.Princess;
import de.swe.oo.server.messages.ErrorMessage;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Integer.parseInt;

public class LoveLetterGame extends Game implements GameLogic{
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;

    protected CopyOnWriteArrayList<Player> activePlayers;

    protected Player playerInCurrentTurn = null;

    protected int round = 0;

    private Deck deck;

    public LoveLetterGame() {
        super(MINPLAYERS, MAXPLAYERS);
        switch (this.players.size()){
            case 2:
                this.targetAffection = 7;
                break;
            case 3:
                this.targetAffection = 5;
                break;
            case 4:
                this.targetAffection = 4;
                break;
        }
    }

    @Override
    public void run() {
        sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
        roundInitialize();
        while (isGoingOn) {
            handleTurn();
            ifRoundEnd();
            turnEnd();
        }
        cleanUp();
    }

    public void handleTurn() {
        Player player = playerInCurrentTurn;
        sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + "'s turn."));
        getPlayerInCurrentTurn().resetIsProtected();  //the effect of handmaid expired
        deck.draw(player);  //player draws card
        String[] options = player.showHands().trim().split(" ");  //use space to split the String hands to Array
        player.requestFromPlayer(new GameChoiceRequestMessage("Please choose a card to discard", options));
        waitForAllResponses();
        int responseIndex = parseInt(player.getLastResponse().trim());
        int response = parseInt(options[responseIndex]);
        changeScore(player, response);
        announceScore();
    }

    @Override
    public void reorderPlayers() {
        int temp = round;  //first set a temporary variable equals the current round
        activePlayers = new CopyOnWriteArrayList<>();  //new an empty activePlayers List
        //The player who is in front of the list, the closer he had a date with princess (lastDateOfDate is bigger)
        //and if there are some players which have the same lastDateOfDate (tie)
        //the order is the same as the order in which the game was added
        //because the order of Players list will never be changed, so the players who was most recently on a date will always go first
        while(temp >= 0){
            for(Player player : players){
                if(player.getLastDateOfDate() == temp){
                    activePlayers.add(player);
                    player.setCurrentgame(this);
                }
            }
            temp--;
        }
        setPlayerInCurrentTurn();
    }

    public void turnEnd(){  //current player will go to the end of activePlayers, and reset the playerInCurrentTurn
        activePlayers.remove(getPlayerInCurrentTurn());
        activePlayers.add(getPlayerInCurrentTurn());
        setPlayerInCurrentTurn();  //next player will take his or her turn
    }

    public void ifRoundEnd(){  //check if a round end or not
        if(getNumberOfActivePlayers() == 1){  //check if there is only one active player
            playerWin(activePlayers.get(0));  //is true, this player will be the winner
            announcePlayersInfo();
            ifGameEnd();  //check if the whole game end or not
            return;
        }
        if(deck.getRemainingCards().size() == 0){  //check whether the deck of cards is empty or not
            announceScore();  //if true, show the scores of each player
            int tempScore = 0;  //a temporary variable to save the highest score
            ArrayList<Player> tempWinner = new ArrayList<>();  //because the winner could be more than one, so use a ArrayList
            for(Player player : activePlayers){  //find out the highest score
                if(scoreMap.get(player) >= tempScore){
                    tempScore = scoreMap.get(player);
                }
            }
            for(Player player : activePlayers){  //find out all winners
                if(scoreMap.get(player) == tempScore){
                    tempWinner.add(player);
                }
            }
            for(Player player : tempWinner){  //for loop to deal with the winners
                playerWin(player);
            }
            announcePlayersInfo();
            ifGameEnd();  //check if game end
            return;
        }
        return;
    }

    public void playerWin(Player player){  //when a player wins, this method will be called
        sendToAllPlayers(new GameAnnounceMessage(player.getName() + "win!"));  //announce who wins
        player.setLastDateOfDate(this.getRound());  //the winner will have a date on the day "round" with princess
        player.setAffectionTockens(player.getAffectionTockens() + 1);  //winner's affection tokens +1
    }

    public void initializeDeck(){
        this.deck = new Deck(this);
        //create 16 specific cards (like new Princess), add them to deck
        for(int i = 16; i > 0; i--){
            deck.getRemainingCards().add(new Princess(this));  //for testing
        }
        switch(activePlayers.size()){  //remove some cards
            case 2:
                deck.removeCard();
                deck.removeCard();
                deck.removeCard();
                break;

            case 3:
                deck.removeCard();
                deck.removeCard();
                break;

            case 4:
                deck.removeCard();
                break;
        }
    }
    public void roundInitialize(){  //initialize a new round
        initializeScores();  //initialize the score
        reorderPlayers();  //reorder the players
        initializeDeck();  //initialize the card deck
        round++;  //round +1
    }
    public void ifGameEnd(){  //check if the whole game end or not
        for(Player player : players){  //check whether there is a player achieve the target number of the affection tokens
            if(player.getAffectionTockens() == targetAffection){  //if true
                sendToAllPlayers(new GameAnnounceMessage  //announce who is the final winner
                    (player.getName() + "is the final winner"));
                isGoingOn = false;  //the game will not be going on
                return;
            }
        }
        roundInitialize();  //because this method is only called by ifRoundEnd() inside the TRUE branch
                       //it means only a round ends, this method will be called
                       //so at the end of this method, initialize a new round is necessary when the game not ends
    }

    public Player getPlayerInCurrentTurn() {
        return playerInCurrentTurn;
    }

    public void setPlayerInCurrentTurn() {
        this.playerInCurrentTurn = activePlayers.get(0);
    }

    public void playerKickedOff(Player targetPlayer){  //kick off a player in a certain round
        int pointer = 0;  //temp variable to traverse the activePlayers List
        while(pointer < this.getNumberOfActivePlayers()){  //traverse the activePlayers
            if(activePlayers.get(pointer).getName().equals(targetPlayer.getName())){  //if the current pointed player is the targetPlayer
                sendToAllPlayers(new GameAnnounceMessage
                    (targetPlayer.getName() + "is out of this round"));  // announce who is kicked off
                activePlayers.remove(pointer);  //remove this player in the activePlayers List
                setPlayerInCurrentTurn();  //to prevent that the removed player is the head of activePlayers
                return;  //end the method
            }
            pointer ++;  //point to the next active player
        }
        sendToAllPlayers(new ErrorMessage  //if the target player is not active
            (targetPlayer.getName() + "is not active in the current game"));
    }

    public int getRound() {
        return round;
    }

    public int getNumberOfActivePlayers(){
        return activePlayers.size();
    }

    public void announceRoundInfo(){  //show to everyone the used cards (also the removed cards at the beginning of a round)
        String result = "used cards are: ";
        for(Card card : getDeck().getUsedCards()){
            result = result + card.getName() + ", ";
        }
        sendToAllPlayers(new GameAnnounceMessage(result));
    }

    public String getNameOfActivePlayers(){  //this method will return a String
        int i = 1;
        String result = "";
        String temp;
        for(Player player : activePlayers){
            temp = "player " + i + ": " + player.getName() + "/n";
            result = result + temp;
            i++;
        }
        return result;
    }

    public String choosePlayerDeck(Player targetPlayer){
        return targetPlayer.showHands();
    }

    //when a player uses some kind of cards(like Baron), it is required to choose a certain player
    //but calling this method has a risk to return a null
    //maybe this problem could be handled by using a while(true) loop, to check the output of this method
    //before really using it, and ask for another choose when it returns null
    public Player getPlayer(String targetName) {  //to get a certain player
                                        // this method needs a Sting name as variable and returns a Player object
        for(Player player : activePlayers){  //traverse the active players to check whether the target player is active
            if(player.getName().equals(targetName)){
                if(player.getIsProtected()){  //check if this player was protected by Handmaid
                    //sendMessage(new GameAnnounceMessage  //if true
                    //("this player was protected by Handmaid, please choose another player"));
                    return null;
                }
                return player;
            }
        }
        return null;
    }

    public Deck getDeck() {
        return deck;
    }
}
