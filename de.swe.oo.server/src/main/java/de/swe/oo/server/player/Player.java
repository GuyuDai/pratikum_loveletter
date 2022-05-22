package de.swe.oo.server.player;


import de.swe.oo.server.cards.Card;
import de.swe.oo.server.game.Game;
import de.swe.oo.server.game.LoveLetterGame;
import de.swe.oo.server.messages.ErrorMessage;
import de.swe.oo.server.messages.GameAnnounceMessage;
import de.swe.oo.server.messages.GameRequestMessage;
import de.swe.oo.server.messages.Message;
import de.swe.oo.server.session.Session;

import java.net.Socket;
import java.util.ArrayList;

public class Player {
    private String name;

    private int lastDateOfDate = 0;  //a certain player had the last date with princess on day <lastDateOfDate>

    private int affectionTockens = 0;

    private ArrayList<Card> hands;


    private Session session;
    public Connection connection;
    public ConnectionListener connectionListener;


    private boolean pendingRequestExists;
    private String lastResponse;

    private boolean isProtected = false;

    private LoveLetterGame currentgame = null;

    public boolean pendingRequestExists() {
        synchronized (this) {
            return pendingRequestExists;
        }
    }

    public Player(Session session, String name, Socket socket) {
        this.session = session;
        this.name = name;
        this.connection = new Connection(socket);
        this.connectionListener = new ConnectionListener(this);
        this.connectionListener.start();
        String threadName = name.concat("_Listener");
        this.connectionListener.setName(threadName);
        this.pendingRequestExists = false;
        this.lastResponse = "No client responses yet.";
    }

    public String getName() {
        return name;
    }

    public Session getSession() {
        return session;
    }

    public void sendMessage(Message msg) {
        connection.sendLine(msg.output());
    }

    public void quit() {
        session.remove(this);
        connectionListener.close();
        connection.close();
    }

    public boolean requestFromPlayer(GameRequestMessage msg) {
        synchronized (this) {
            if (!pendingRequestExists) {
                sendMessage(msg);
                pendingRequestExists = true;
                return true;
            }
            return false;
        }
    }

    public void processResponse(String response) {
        synchronized (this) {
            if (pendingRequestExists) {
                lastResponse = response;
                pendingRequestExists = false;
                sendMessage(new GameAnnounceMessage("Received an expected input."));
            } else {
                sendMessage(new ErrorMessage("Received an unexpected message."));
            }
        }
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public int getLastDateOfDate() {
        return lastDateOfDate;
    }

    public void setLastDateOfDate(int lastDateOfDate) {
        this.lastDateOfDate = lastDateOfDate;
    }

    public int getAffectionTockens() {
        return affectionTockens;
    }

    public void setAffectionTockens(int affectionTockens) {
        this.affectionTockens = affectionTockens;
    }

    public String[] showHands(){  //return the cards from player's hand in String type
        String temp = "";
        for(Card card : hands){
            temp = temp + card.getName() + " ";
        }
        return temp.trim().split(" ");
    }

    public void setHands(Card card) {
        hands.add(card);
    }

    public Card getHands(int i) {
        return hands.get(i);
    }

    public int getHandsSize(){
        return hands.size();
    }


    public boolean getIsProtected(){
        return isProtected;
    }

    public void resetIsProtected(){
        isProtected = false;
    }

    public Game getCurrentgame() {
        return currentgame;
    }

    public void setCurrentgame(LoveLetterGame currentgame) {
        this.currentgame = currentgame;
    }

    public void discard(Card card){
        card.effect();
        hands.remove(card);
        this.currentgame.getDeck().getUsedCards().add(card);
    }

    public void handInitialize(){
        this.hands = new ArrayList<>(2);
    }
}
