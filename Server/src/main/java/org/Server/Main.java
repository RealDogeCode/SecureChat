package org.Server;

import org.apache.commons.cli.*;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.*;

public class Main {

    public static Logger serverLogger = Logger.getLogger("Logger");
    static boolean running = true;
    public static void main(String[] args) throws ParseException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter());
        serverLogger.addHandler(handler);
        serverLogger.setUseParentHandlers(false);

        Options options = new Options();
        Option p = new Option("p", "port", true, "port bound to the socket server.");
        Option a = new Option("a", "address", true, "address bound to the socket server.");

        p.setRequired(true);
        p.setType(Integer.class);
        options.addOption(p);
        options.addOption(a);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Server", options);
            System.exit(1);
        }

        int port = cmd.getParsedOptionValue("port");
        String address = cmd.getParsedOptionValue("address");

        try(ServerSocket serverSocket = new ServerSocket(port, 5, InetAddress.getByName(address)))
        {
            serverLogger.info("Initializing server on " + serverSocket.getInetAddress().getHostAddress() + ":" + port);

            while(running) {
                Socket clientSocket = serverSocket.accept();

                serverLogger.info(clientSocket.getInetAddress() + " Has Joined The Main.");

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                UserThread userThread = new UserThread(clientSocket, in, out);
                userThread.start();
            }
        }
        catch (Exception e)
        {
            serverLogger.severe(e.getMessage());
        };
    }
}


class UserThread extends Thread{
    private final BufferedReader in;
    private final PrintWriter out;
    private final Socket socket;
    public UserThread(Socket socket, BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
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
                    Main.serverLogger.info(this.socket.getInetAddress() + ":" + this.socket.getLocalPort() + " said " + message);
                }
            } catch (IOException e) {
                if(e.getClass() == SocketException.class){
                    Main.serverLogger.info(this.socket.getInetAddress() + " Disconnected.");
                    break;
                }
            }
        }
        this.interrupt();
    }
}