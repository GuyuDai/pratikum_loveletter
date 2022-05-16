package de.swe.oo.server.game;

import de.swe.oo.server.cards.Card;
import de.swe.oo.server.messages.ErrorMessage;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameChoiceRequestMessage;
import de.swe.oo.server.player.Player;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Integer.parseInt;

public class LoveLetterGame extends Game {
    private static int MINPLAYERS = 2;
    private static int MAXPLAYERS = 4;

    protected CopyOnWriteArrayList<Player> activePlayers;

    protected Player playerInCurrentTrun = null;

    protected int round = 0;

    private ArrayList<Card> deck;

    private ArrayList<Card> usedCards;

    public LoveLetterGame() {
        super(MINPLAYERS, MAXPLAYERS);
    }

    @Override
    public void run() {
        sendToAllPlayers(new GameAnnounceMessage("The Game has been started successfully!"));
        initialize();
        while (isGoingOn) {
            handleTurn(getPlayerInCurrentTrun());
            ifRoundEnd();
            turnEnd();
        }
        cleanUp();
    }

    protected void handleTurn(Player player) {
        sendToAllPlayers(new GameAnnounceMessage("It's " + player.getName() + "'s turn."));
        String[] options = {"1", "12", "-1"};
        player.requestFromPlayer(new GameChoiceRequestMessage("Please choose one of the numbers.", options));
        waitForAllResponses();
        int responseIndex = parseInt(player.getLastResponse().trim());
        int response = parseInt(options[responseIndex]);
        changeScore(player, response);
        announceScore();
    }

    @Override
    protected void reorderPlayers() {
        int temp = round;
        activePlayers = new CopyOnWriteArrayList<>();
        while(temp >= 0){
            for(Player player : players){
                if(player.getLastDateOfDate() == temp){
                    activePlayers.add(player);
                }
            }
            temp--;
        }
        setPlayerInCurrentTrun();
    }

    public void turnEnd(){  //current player will go to the end of activePlayers, and reset the playerInCurrentTurn
        activePlayers.remove(getPlayerInCurrentTrun());
        activePlayers.add(getPlayerInCurrentTrun());
        setPlayerInCurrentTrun();
    }

    public void ifRoundEnd(){
        if(getNumberOfActivePlayers() == 1){
            playerWin(activePlayers.get(0));
            announcePlayersInfo();
            ifGameEnd();
            return;
        }
        if(deck.size() == 0){
            announceScore();
            int tempScore = 0;
            ArrayList<Player> tempWinner = new ArrayList<>();
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
            for(Player player : tempWinner){  //players win
                playerWin(player);
            }
            announcePlayersInfo();
            ifGameEnd();
            return;
        }
        return;
    }

    public void playerWin(Player player){
        sendToAllPlayers(new GameAnnounceMessage(player.getName() + "win!"));
        player.setLastDateOfDate(this.getRound());
        player.setAffectionTockens(player.getAffectionTockens() + 1);
    }

    public void initializeDeck(){

    }
    protected void initialize(){
        initializeScores();
        reorderPlayers();
        initializeDeck();
        round++;
    }
    public void ifGameEnd(){
        for(Player player : players){
            if(player.getAffectionTockens() == targetAffection){
                sendToAllPlayers(new GameAnnounceMessage
                        (player.getName() + "is the final winner"));
                isGoingOn = false;
                return;
            }
        }
        initialize();
    }

    public Player getPlayerInCurrentTrun() {
        return playerInCurrentTrun;
    }

    public void setPlayerInCurrentTrun() {
        this.playerInCurrentTrun = activePlayers.get(0);
    }

    public void playerKickedOff(Player targetPlayer){
        int pointer = 0;
        while(pointer < this.getNumberOfActivePlayers()){
            if(activePlayers.get(pointer).getName().equals(targetPlayer.getName())){
                sendToAllPlayers(new GameAnnounceMessage
                        (targetPlayer.getName() + "failed"));
                activePlayers.remove(pointer);
                setPlayerInCurrentTrun();  //to prevent that the removed player is the head of activePlayers
                return;
            }
            pointer ++;
        }
        sendToAllPlayers(new ErrorMessage
                (targetPlayer.getName() + "is not active in the current game"));
    }

    public int getRound() {
        return round;
    }

    public int getNumberOfActivePlayers(){
        return activePlayers.size();
    }

    public void announceRoundInfo(){
        String result = "used cards are: ";
        for(Card card : usedCards){
            result = result + card.getName() + ", ";
        }
        sendToAllPlayers(new GameAnnounceMessage(result));
    }
}
