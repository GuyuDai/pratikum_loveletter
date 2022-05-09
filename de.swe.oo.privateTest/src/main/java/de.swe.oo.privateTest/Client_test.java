package de.swe.oo.privateTest;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client_test extends Thread {
    private final Socket clientSocket;
    private ArrayList<Client_test> activeClients;
    private PrintWriter outputCommunication;
    private BufferedReader inputCommunication;
    private String clientName;

    public Client_test(Socket clientSocket, ArrayList<Client_test> activeClients) {
        this.clientSocket = clientSocket;
        this.activeClients = activeClients;
    }

    @Override
    public void run() {
        try {
            inputCommunication = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outputCommunication = new PrintWriter(clientSocket.getOutputStream(), true);

            if(!getClientsNames().contains(this.clientName = inputCommunication.readLine())) {
                System.out.println(clientName + " joined the chat.");
                outputCommunication.println("Welcome " + clientName + " to the best chat ever!");
                synchronized (this) {
                    for (Client_test client : activeClients) {
                        client.outputCommunication.println("SERVER: " + clientName + " entered the chat!");
                    }
                }

                activeClients.add(this);

                while (isAlive()) {
                    String inputLine = inputCommunication.readLine();
                    if (inputLine.contains("bye")) {
                        clientLeave();
                        break;
                    } else synchronized (this) {
                        sendToEveryone(clientName + ": " + inputLine); //sending the messages here to everyone
                    }
                }
            } else {
                outputCommunication.println("Nickname is already used!");
                quit();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }


    private void sendToEveryone(String message){
        for (Client_test client : activeClients)
            client.outputCommunication.println(message);
    }


    private void clientLeave() throws IOException {
        activeClients.remove(this);
        quit();
        sendToEveryone(clientName + " left the room.");
        }


    private String getClientsNames() {
        StringBuilder clientList = new StringBuilder();
        clientList.append("/beginOfClient" + "\n");
        for (Client_test client : activeClients)
            clientList.append(client.clientName).append("\n");
        clientList.append("/endOfClient" + "\n");
        return clientList.toString();
    }
    private void quit() throws IOException {
        outputCommunication.println("Bye " + clientName + "!");
        outputCommunication.println("/endOfTransmission");
        outputCommunication.close();
        inputCommunication.close();
        clientSocket.close();

    }
}
