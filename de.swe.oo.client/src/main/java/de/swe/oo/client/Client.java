package de.swe.oo.client;

public abstract class Client extends Thread {
    abstract public void sendText(String text);

    abstract public void outputChat(String text);

    abstract public void close();
}
