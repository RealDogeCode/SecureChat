package net.securechat;
import net.securechat.Client.ClientApp;
import net.securechat.Server.ServerApp;
import net.securechat.util.CLIParser;
import org.apache.commons.cli.*;

import java.util.logging.Logger;

public class Main {
    public static Logger LOGGER = Logger.getLogger("SecureChat");
    public static void main(String[] args) throws ParseException {
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tF %1$tT] [%4$s] %5$s %n");

        CommandLine cmd = CLIParser.parseArgs(args);

        boolean isServer = cmd.hasOption("isServer");
        int port = cmd.hasOption("port") ? cmd.getParsedOptionValue("port") : 6969;
        String address = cmd.hasOption("address") ? cmd.getParsedOptionValue("address") : "127.0.0.1";

        if(isServer){
            ServerApp.run(address, port);
        }
        else
        {
            ClientApp.run(address, port);
        }
    }
}

