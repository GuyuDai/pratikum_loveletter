package de.swe.oo.server.game;

import de.swe.oo.server.cards.*;
import de.swe.oo.server.messages.ErrorMessage;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;
import java.util.Collections;
import java.util.List;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Integer.parseInt;

public class LoveLetterGame extends Game implements GameLogic{
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;

    protected int targetAffection;

    protected CopyOnWriteArrayList<Player> activePlayers;

    private List<Player> currentPlayers;

    protected Player playerInCurrentTurn = null;

    protected int round = 0;

    private Deck deck;

    public LoveLetterGame() {
        super(MINPLAYERS, MAXPLAYERS);
    }

    @Override
    public synchronized boolean startGame() {
        if (players.size() >= this.minPlayers) {
            isGoingOn = true;
            this.start();
            this.setName("LoveLetterGameThread");
            return true;
        }
        return false;
    }

    /**
     * @author dai
     * when a game started successfully, first initialize the round,
     * and go into the while loop if the game is still going on
     * in the while loop, we handle the behavior of a player
     * after the turn ends, we will check whether the rounds end or the whole game ends
     */
    @Override
    public void run() {
        sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
        roundInitialize();
        while (isGoingOn) {
            handleTurn();
            turnEnd();
            ifRoundEnd();
        }
        cleanUp();
    }

    /**
     * @author dai
     *this method is used to represent the behavior of a player in a turn
     */
    public void handleTurn() {
        Player player = playerInCurrentTurn;
        sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + " 's turn."));
        getPlayerInCurrentTurn().resetIsProtected();  //the effect of handmaid expired
        deck.draw(player);  //player draws card
        String[] options = player.showHands();  //use space to split the String hands to Array
        player.requestFromPlayer(new GameChoiceRequestMessage("Please choose a card to discard", options));
        waitForAllResponses();
        int responseIndex = parseInt(player.getLastResponse().trim());  //player enter the index of which card the player want to discard
        Card response = player.getHands(responseIndex);  //get this card
        changeScore(player, response.getValue());  //score up
        player.discard(response);  //discard
        announceScore();
    }

    /**
     * @author dai
     * reorder the active player list regarding the last date with princess of each player
     * if there is a tie, the order will be the same as the order in which the game was added
     */
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
                    player.handInitialize();
                }
            }
            temp--;
        }
        setPlayerInCurrentTurn();
    }

    /**
     * @author dai
     * initialize the target number of the affection tokens
     */
    public void affectionInitialize(){
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
        //System.out.println("target affection : " + targetAffection);  //for testing
    }

    /**
     * @author dai
     * when a turn ends, the player in current turn will be removed to the end of the list of active players
     * and the player in current turn will be reseted
     */
    public void turnEnd(){  //current player will go to the end of activePlayers, and reset the playerInCurrentTurn
        activePlayers.remove(getPlayerInCurrentTurn());
        activePlayers.add(getPlayerInCurrentTurn());
        setPlayerInCurrentTurn();  //next player will take his or her turn
    }

    /**
     * @author dai
     * check if a round ends, if true, handle the behavior of the winner
     * then check if the whole game ends
     */
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

    /**
     * @author dai
     * handle the behavior of a winner
     * @param player is the winner
     */
    public void playerWin(Player player){  //when a player wins, this method will be called
        sendToAllPlayers(new GameAnnounceMessage(player.getName() + " wins!"));  //announce who wins
        player.setLastDateOfDate(this.getRound());  //the winner will have a date on the day "round" with princess
        player.setAffectionTockens(player.getAffectionTockens() + 1);  //winner's affection tokens +1
    }

    public void initializeDeck(){
        this.deck = new Deck(this);
        //create 16 specific cards (like new Princess), add them to deck
        for(int i = 16; i > 0; i--){
        //System.out.println("really enter the for loop to initialize the deck");  //for testing

            deck.getRemainingCards().add(
                    new Princess(this));

            deck.getRemainingCards().add(
                    new Countess(this));

            deck.getRemainingCards().add(
                    new King(this));

            deck.getRemainingCards().add(
                    new Prince(this));

            deck.getRemainingCards().add(
                    new Prince(this));

            deck.getRemainingCards().add(
                    new Handmaid(this));

            deck.getRemainingCards().add(
                    new Handmaid(this));

            deck.getRemainingCards().add(
                    new Priest(this));

            deck.getRemainingCards().add(
                    new Priest(this));

            deck.getRemainingCards().add(
                    new Baron(this));

            deck.getRemainingCards().add(
                    new Baron(this));

            deck.getRemainingCards().add(
                    new Guard(this));

            deck.getRemainingCards().add(
                    new Guard(this));

            deck.getRemainingCards().add(
                    new Guard(this));

            deck.getRemainingCards().add(
                    new Guard(this));

            deck.getRemainingCards().add(
                    new Guard(this));
        }
        Collections.shuffle(deck.getRemainingCards());  //disorder the deck
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

    /**
     * @author dai
     * inside this method these things will be down:
     * round adds, initialize the scores of players, target affection tokens, and deck
     * reorder the players, and players draw their first card
     */
    public void roundInitialize(){  //initialize a new round
        round++;  //round +1
        sendToAllPlayers(new GameAnnounceMessage("Round " + round));
        initializeScores();  //initialize the score
        reorderPlayers();  //reorder the players
        affectionInitialize();  //initialize the target of affection tokens
        initializeDeck();  //initialize the card deck
        for(Player player : activePlayers){  //everyone draws a card
            deck.draw(player);
            player.sendMessage(new GameAnnounceMessage
                ("your first card is " + player.getHands(0).getName()));
        }
    }

    /**
     * @author dai
     * check if the game ends, if not true, a new round will be initialized
     */
    public void ifGameEnd(){  //check if the whole game end or not
        for(Player player : players){  //check whether there is a player achieve the target number of the affection tokens
            if(player.getAffectionTockens() == targetAffection){  //if true
                sendToAllPlayers(new GameAnnounceMessage  //announce who is the final winner
                    (player.getName() + " is the final winner"));
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

    /**
     * @author dai
     * @param targetPlayer this variable player will be removed out of the active players list
     */
    public void playerKickedOff(Player targetPlayer){  //kick off a player in a certain round
        int pointer = 0;  //temp variable to traverse the activePlayers List
        while(pointer < this.getNumberOfActivePlayers()){  //traverse the activePlayers
            if(activePlayers.get(pointer).getName().equals(targetPlayer.getName())){  //if the current pointed player is the targetPlayer
                sendToAllPlayers(new GameAnnounceMessage
                    (targetPlayer.getName() + " is out of this round"));  // announce who is kicked off
                activePlayers.remove(pointer);  //remove this player in the activePlayers List
                setPlayerInCurrentTurn();  //to prevent that the removed player is the head of activePlayers
                return;  //end the method
            }
            pointer ++;  //point to the next active player
        }
        sendToAllPlayers(new ErrorMessage  //if the target player is not active
            (targetPlayer.getName() + " is not active in the current game"));
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
    public List<String> getNameOfActivePlayers(){
        List<String> activeplayers_name = new ArrayList<>();
        for(Player player : activePlayers){
            activeplayers_name.add(player.getName());
        }
        return activeplayers_name;

    }

    public void addCurrentPlayer(Player activePlayer){
        currentPlayers.add(activePlayer);
    }

    public List<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    public void removeCurrentPlayer(Player activePlayer){
        for (int i = 0; i < currentPlayers.size(); i++) {
            if (currentPlayers.get(i) == activePlayer){
                currentPlayers.remove(i);
            }
        }
    }

    /**
     * @author dai
     * this method is better not to use anymore
     * @param targetPlayer
     * @return
     */
    public String choosePlayerDeck(Player targetPlayer){
        return targetPlayer.showHands().toString();
    }

    /**
     * @author dai
     * @param targetName is the name of the player who you want to choose
     * @return the target player as type Player
     */
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
