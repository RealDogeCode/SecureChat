package net.securechat.util;

import org.apache.commons.cli.*;

public class CLIParser {
    public static CommandLine parseArgs(String[] args) {
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

        return cmd;
    }
}
