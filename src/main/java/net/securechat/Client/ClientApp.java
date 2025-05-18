package net.securechat.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    public static void run(String address, int port) {
        try(Socket socket = new Socket()){
            socket.connect(new InetSocketAddress(address, port));

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            ReceiveMessage receiveMessage = new ReceiveMessage(in);
            receiveMessage.start();

            Scanner scanner = new Scanner(System.in);
            while(receiveMessage.isAlive()) {
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

class ReceiveMessage extends Thread {
    private final BufferedReader in;
    private volatile boolean running = true;

    public ReceiveMessage(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (running) {
                String line = in.readLine();
                System.out.println(line);
                if (line == null) {
                    System.out.println("Server ha chiuso la connessione.");
                    running = false;
                }
            }
        } catch (IOException e) {
            System.out.println("Connessione persa con il server: " + e.getMessage());
            running = false;
        }
    }
}
