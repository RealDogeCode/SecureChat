package org.SecureChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Server {
    static boolean running = true;
    public static Logger serverLogger = Logger.getLogger("Logger");
    public Server(String address, int port){
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        serverLogger.addHandler(handler);
        serverLogger.setUseParentHandlers(false);

        try(ServerSocket serverSocket = new ServerSocket(port, 5, InetAddress.getByName(address)))
        {
            serverLogger.info("Initializing server on " + serverSocket.getInetAddress().getHostAddress() + ":" + port);

            while(running) {
                Socket clientSocket = serverSocket.accept();

                serverLogger.info(clientSocket.getInetAddress() + " Has Joined The Server.");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                UserThread userThread = new UserThread(clientSocket, in, out);
                userThread.start();
            }
        }
        catch (Exception e)
        {
            serverLogger.severe(e.getMessage());
        }
    }
}

class UserThread extends Thread{
    private final BufferedReader in;
    private final Socket socket;
    public UserThread(Socket socket, BufferedReader in, PrintWriter out){
        this.in = in;
        this.socket = socket;
    }
    @Override
    public void run() {
        while(true){
            try {
                String message;

                if(this.socket.isClosed()){
                    this.interrupt();
                }

                if ((message = in.readLine()) != null) {
                    Server.serverLogger.info(this.socket.getInetAddress() + ":" + this.socket.getLocalPort() + " said " + message);
                }
            } catch (IOException e) {
                if(e.getClass() == SocketException.class){
                    Server.serverLogger.info(this.socket.getInetAddress() + " Disconnected.");
                    break;
                }
            }
        }
        this.interrupt();
    }
}