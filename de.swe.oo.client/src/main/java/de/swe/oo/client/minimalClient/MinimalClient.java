package de.swe.oo.client.minimalClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class MinimalClient extends Thread {
    Socket clientSocket;
    private String ip;
    private int port;
    Listener listener;
    PrintWriter out;
    BufferedReader in;

    public MinimalClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        int finalPort = login();
        connect(finalPort);
    }

    public int login(){
        try {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            listener = new Listener(in);
        } catch (IOException e) {
            System.out.println("An error occurred while opening IO objects for login. " + e.getMessage());
            return -1;
        }
        String userName = "defaultName";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Gewuenschten Nutzernamen eingeben:");
        try {
            userName = br.readLine();
        } catch (IOException e) {
            System.out.println("A problem occurred while reading input from the terminal. " + e.getMessage());
        }
        out.println(userName);
        String answer;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            System.out.println("Error receiving the User port. " + e.getMessage());
            return -1;
        }
        int finalPort = Integer.parseInt(answer);
        return finalPort;
        }

    public void connect(int finalPort){
        if (finalPort < 0){
            System.out.println("An error occured while requesting port.");
            return;
        }
        while(true) {
            try {
                sleep(20);
                clientSocket = new Socket(ip, finalPort);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                listener = new Listener(in);
                break;
            } catch (IOException e) {
                System.out.println("An error occurred while opening IO objects. " + e.getMessage());
            } catch(Exception e){
                System.out.println("Error while connecting to final port. " + e.getMessage());
            }
        }
        listener.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String currentInput = "";
        while (currentInput != null && listener.running) {
            System.out.println("Nachricht eingeben:");
            try {
                currentInput = br.readLine();
            } catch (IOException e) {
                System.out.println("A problem occurred while reading input from the terminal. " + e.getMessage());
                continue;
            }
            out.println(currentInput);
        }
    }

    public static void main(String[] args) {
        MinimalClient client = new MinimalClient("localhost", 4444);
        client.start();
    }
}
