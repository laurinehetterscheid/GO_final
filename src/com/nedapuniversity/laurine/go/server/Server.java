/**
 * 
 */
package com.nedapuniversity.laurine.go.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author laurine.hetterscheid
 *
 */
public class Server {

    private ServerSocket serverSocket;
    private boolean running = true;

    // Keep track off all games that are being played vioa their handlers
    private List<GameHandler> gameHandlers;


    /**
     * Creates a new server with a given server connection
     * @param theServerSocket The connection to listen on for new clients
     */
    public Server(ServerSocket theServerSocket) {
        serverSocket = theServerSocket;
        gameHandlers = new ArrayList<>();
    }


    /**
     * Accepts incoming client connections and joins 2 clients in a new game
     */
    public void start() {
        System.out.println("GO Server started, waiting for incoming connections");
        while (running) {
            try {
                // Wait new client to connect
                Socket clientSocket = serverSocket.accept();

                System.out.println("New client connected " + clientSocket.toString());

                // Create client socket in/out
                ClientHandler nextClient = new ClientHandler(clientSocket);

                // Find game for next client to play in
                addClientHandler(nextClient);
            }
            catch (IOException exception) {
                System.err.println("Error when listening for client requests " + exception.getMessage());
                System.exit(1);
            }
        }

        try {
            System.out.println("Closing server socket listener");
            serverSocket.close();
        }
        catch (IOException exception) {
            System.err.println("Failed to close server socket connection after stopping " + exception.getMessage());
            System.exit(1);
        }

        // All finished now
        System.out.println("Thanks for playing GO!!!");
    }

    private GameHandler addClientHandler(ClientHandler client) {
        // If we have a previous game, check if it has room for another player, otherwise return a new GameHandler
        if (!gameHandlers.isEmpty()) {
            GameHandler lastGameHandler = gameHandlers.get(gameHandlers.size() - 1);
            if (!lastGameHandler.isFull()) {
                lastGameHandler.addOpponent(client);
                return lastGameHandler;
            }
        }

        // Create a new game and have it start listening for input on the provided socket
        GameHandler gameHandler = new GameHandler(client);

        // Start a new Game
        int gameId = gameHandlers.size();
        gameHandler.startGame(gameId);

        System.out.println("Starting new game with id " + gameId);
        gameHandlers.add(gameHandler);
        return gameHandler;
    }
}
