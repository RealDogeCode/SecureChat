package org.SecureChat;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public Client(String address, int port){
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(address, port));

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            IsAliveThread isAliveThread = new IsAliveThread(in);
            isAliveThread.start();

            Scanner scanner = new Scanner(System.in);
            while(isAliveThread.isAlive()) {
                String input = scanner.nextLine();
                out.println(input);
            }

            System.out.println("Server closed.");
        }catch (Exception e)
        {
            if(e instanceof ConnectException){
                System.out.println("Connessione Rifiutata dall'host.");
            }
        }
    }
}

class IsAliveThread extends Thread {
    private final BufferedReader in;
    private volatile boolean running = true;

    public IsAliveThread(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (running) {
                int data = in.read();
                if (data == -1) { // stream chiuso
                    running = false;
                }
            }
        } catch (IOException e) {
            running = false;
        }
    }

    public boolean isRunning() {
        return running;
    }
}
