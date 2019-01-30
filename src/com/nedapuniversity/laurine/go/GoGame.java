/**
 *
 */
package com.nedapuniversity.laurine.go;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import com.nedapuniversity.laurine.go.client.Client;
import com.nedapuniversity.laurine.go.game.Player;
import com.nedapuniversity.laurine.go.server.Server;

/**
 * @author laurine.hetterscheid
 *
 */
public class GoGame {

    private static final String CLIENT_ARG = "-client";
    private static final String SERVER_ARG = "-server";
    private static final String HELP_ARG = "-help";


    /**
     * Usage  java GoGame -client Laurine 123.123.123.123 9000
     *  ..or  java GoGame -server 9000
     *  ..or  java GoGame -help
     * @param args
     */
    public static void main(String[] args) {

        if (args.length >= 1) {

            switch (args[0]) {
               case CLIENT_ARG:
                   startClient(args);
                   break;
               case SERVER_ARG:
                   startServer(args);
                   break;
               default:
               case HELP_ARG:
                   printHelp();
                   break;
            }
        }
        else {
            printHelp();
        }
    }

    private static void printHelp() {
        System.out.println("Usage for GO game: ");
        System.out.println("- to start server:   'java GoGame -server <port>' ");
        System.out.println("- to start client:   'java GoGame -client <player_name> <ip_address> <port> <player_type>' ");
        System.out.println("- to show this help: 'java GoGame -help ");
    }


    private static void startClient(String[] args) {

        if (args.length != 5) {
            System.err.println("Did not receive expected number of arguments, exiting...");
            printHelp();
        }

        InetAddress address = null;
        int port = 0;
        Socket socket = null;

        try {
            address = InetAddress.getByName(args[2]);
        }
        catch (UnknownHostException exception) {
            System.err.println("Could not find specified IP address " + args[2]);
            System.exit(2);
        }

        try {
            port = Integer.parseInt(args[3]);
        }
        catch (NumberFormatException exception) {
            System.err.println("Could not parse specified port number " + args[3]);
            System.exit(2);
        }

        String playerType = args[4].toLowerCase();
        if (!playerType.equalsIgnoreCase(Player.HUMAN) && !playerType.equalsIgnoreCase(Player.AI)) {
            System.err.println("Did not recognize player type as '" + Player.HUMAN + "' or '" + Player.AI + "' from " + args[4]);
            System.exit(2);
        }

        try {
            System.out.println("Connecting client '" + args[1] + "' to server at " + address.getHostAddress() + ":" + port + " as type " + playerType);
            socket = new Socket(address, port);

            Client client = new Client(args[1], socket, playerType);
            client.start();
        }
        catch (IOException exception) {
            System.err.println("Failed to open socket connection to specified server address " + address.getHostAddress() + ":" + port);
            System.exit(2);
        }

    }

    private static void startServer(String[] args) {

        if (args.length != 2) {
            System.err.println("Did not receive expected number of arguments, exiting...");
            printHelp();
        }

        int port = 0;

        try {
            port = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException exception) {
            System.err.println("Could not parse specified port number " + args[1]);
            System.exit(1);
        }

        // Create socket listener and start server
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            Server server = new Server(serverSocket);
            server.start();
        }
        catch (IOException exception) {
            System.err.println("Failed to start server socket listener on specified port: " + port);
            System.exit(1);
        }

        // TODO: set up server further
    }
}
