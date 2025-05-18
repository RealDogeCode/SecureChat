package org.SecureChat;

import org.apache.commons.cli.*;

public class Main {
    public static void main(String[] args) throws ParseException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");

        Options options = new Options();
        Option s = new Option("s", "isServer", false, "Is server.");
        Option p = new Option("p", "port", true, "port bound to the socket server.");
        Option a = new Option("a", "address", true, "address bound to the socket server.");

        p.setType(Integer.class);

        options.addOption(s);
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

        boolean isServer = cmd.hasOption("isServer");
        int port = cmd.hasOption("port") ? cmd.getParsedOptionValue("port") : 6969;
        String address = cmd.hasOption("address") ? cmd.getParsedOptionValue("address") : "127.0.0.1";

        if(isServer){
            runServer(address, port);
        }
        else
        {
            runClient(address, port);
        }
    }

    private static void runServer(String address, int port){
        new Server(address, port);
    }

    private static void runClient(String address, int port){
        new Client(address, port);
    }
}