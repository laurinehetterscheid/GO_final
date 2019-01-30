package com.nedapuniversity.laurine.go.server;

import com.nedapuniversity.laurine.go.commands.*;
import com.nedapuniversity.laurine.go.game.Game;
import com.nedapuniversity.laurine.go.game.Move;
import com.nedapuniversity.laurine.go.game.Player;
import com.nedapuniversity.laurine.go.game.Stone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private Socket socket;

    private BufferedReader in;
    private PrintWriter out;

    private GameHandler gameHandler;
    private Player player;
    private boolean running = true;


    /**
     * Creates client handler that holds the connection to 1 player and listens for commands received from this client
     *
     * @param theSocket the connection to the client
     */
    ClientHandler(Socket theSocket) {
        socket = theSocket;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
        }
        catch (IOException exception) {
            System.err.println("Error when listening for client requests " + exception.getMessage());
            System.exit(1);
        }
    }


    /**
     * Sets our game handler that holds the game
     * @param theGameHandler the handler
     */
    void setHandler(GameHandler theGameHandler) {
        gameHandler = theGameHandler;
    }


    /**
     * Starts listening to client requests and passes them to processClientRequest for processing
     */
    @Override
    public void run() {
        while (running) {
            try {
                processClientRequest(in.readLine());
            } catch (IOException exception) {
                System.err.println("Error when listening for client requests " + exception.getMessage());
                System.exit(1);
            }
        }

        try {
            in.close();
            out.close();
        }
        catch (IOException exception) {
            System.err.println("Error when closing socket connection " + exception.getMessage());
        }
    }

    /**
     * Disconnects the client
     */
    void disconnect() {
        running = false;
        try {
            socket.close();
        }
        catch (IOException exception) {
            System.err.println("Failed to close socket connection " + exception.getMessage());
        }
    }

    private void processClientRequest(String input) {
        if (input == null) {
            System.err.println("Ignoring empty input from client; disconnecting");
            disconnect();
            return;
        }
        System.out.println("Processing client request '" + input + "'");

        String[] command = input.split(Command.ESCAPED_DELIMITER);

        if (command.length < 2) {
            // Server send unexpected command, unable to recover
            System.out.println("Unexpected request from client '" + input + "', replying with " + Command.UNKNOWN_COMMAND);
            out.println(new UnknownCommand("Unknown command: " + input));
            return; // stop here
        }

        switch (command[0]) {
            case Command.HANDSHAKE:
                System.out.println("Processing handshake '" + input + "'");
                processHandshake(command);
                break;
            case Command.SET_CONFIG:
                System.out.println("[" + gameHandler.getGame().getId() + "] Processing config '" + input + "'");
                processSetConfig(command);
                break;
            case Command.MOVE:
                System.out.println("[" + gameHandler.getGame().getId() + "] Processing move '" + input + "'");
                processMove(command);
                break;
            default:
                throw new IllegalStateException("Did not recognize client request: " + command[0]);
        }
    }


    private void processHandshake(String[] command) {
        // Should have 1 arg: +$PLAYER_NAME

        String playerName = command[1];

        Game game = gameHandler.getGame();
        int playerId = game.getPlayers().size();
        player = new Player(playerName, playerId);

        System.out.println("[" + game.getId() + "] Processing handshake request for player '" + playerName + "'");

        // TODO: validate if other player doesnt have the same name
        boolean isLeader = false;
        try {
            isLeader = game.addPlayer(player);
        }
        catch (IllegalStateException exception) {
            System.err.println("[" + game.getId() + "] New player has the same name (" + playerName + ") as the leader player, asking for new handshake");
            out.println(new UnknownCommand(UnknownCommand.PLAYER_CANNOT_HAVE_SAME_NAME));
            return;
        }
        System.out.println("[" + game.getId() + "] Processing handshake request for player '" + playerName + "'");

        out.println(new AcknowledgeHandshakeCommand(game.getId(), isLeader));
        System.out.println("[" + game.getId() + "] Handshake acknowledged, player is leader? " + isLeader);

        if (isLeader) {
            // Request leader to send config via: REQUEST_CONFIG
            out.println(new RequestConfigCommand());
            System.out.println("[" + game.getId() + "] Handshake acknowledged, requesting config from leader");
        }
        else if (game.isBoardConfigured()) {
            gameHandler.acknowledgeConfiguration();
        }
    }

    private boolean isValidGameId(int gameId) {
        return gameHandler.getGame().getId() == gameId;
    }


    private void processSetConfig(String[] command) {
        // Should have 3 args: +$GAME_ID+$PREFERRED_COLOR+$BOARD_SIZE
        int gameId = -1;
        int preferredColor = 0;
        int boardSize = 0;

        try {
            gameId = Integer.parseInt(command[1]);
            preferredColor = Integer.parseInt(command[2]);
            boardSize = Integer.parseInt(command[3]);
        }
        catch (NumberFormatException exception) {
            String errorMessage = "gameId, preferredColor and boardSize should all be integers" + command;
            out.println(new UnknownCommand(errorMessage));
            System.err.println(errorMessage);
        }

        if (!isValidGameId(gameId)) {
            System.err.println("Game ID does not match our game: " + gameId);
            return;
        }

        Game game = gameHandler.getGame();
        game.createBoard(boardSize);

        // give leader the preferred color
        List<Player> players = game.getPlayers();
        Player leader = players.get(0);
        leader.setStone(preferredColor);

        if (game.isFullWithPlayers()) {
            gameHandler.acknowledgeConfiguration();
        }
    }

    /**
     * Notify client that the config has been received and all players are connected
     */
    void acknowledgeConfig() {
        String name = player.getName();
        int color = player.getStone().getColorID();

        Game game = gameHandler.getGame();
        int size = game.getBoardSize();

        int blackPlayerId = 0;
        for (Player player : game.getPlayers()) {
            System.out.println("Player " + player.getId() + " has " + player.getStone().getColorDescription() + "/" + player.getStone().getColorID() + " and ID " + player.getId());
            if (player.getStone() == Stone.BLACK) {
                blackPlayerId = player.getId();
            }
        }
        // Set initial game state to playing, current player (== leader) and empty board state
        game.setGameState(GameState.PLAYING, blackPlayerId, game.getBoard().boardStatus());

        String gameState = game.getGameState().toString();

        Player opponent = game.getOpponent(player);
        String opponentName = opponent.getName();

        // Set player's stone if we registered before config was sent
        if (player.getStone() == Stone.EMPTY) {
            player.setStone(opponent.getStone() == Stone.BLACK ? 2 : 1);
        }

        System.out.println("[" + game.getId() + "] Config acknowledged to " + name + ", current player is " + blackPlayerId + ":" + game.getCurrentPlayerId());
        out.println(new AcknowledgeConfigCommand(name, color, size, gameState, opponentName));
    }


    private void processMove(String[] command) {
        //Should have 3 args: +$GAME_ID+$PLAYER_NAME+$TILE_INDEX

        int gameId = Integer.parseInt(command[1]);
        Game game = gameHandler.getGame();

        // Ensure that the messages are addressed to us
        Player player = game.getPlayer(command[2]);
        if (isValidGameId(gameId) && player != null) {

            int tileIndex = Integer.parseInt(command[3]);
            Move desiredMove;
            if (tileIndex == -1) {
                // Player passes
                desiredMove = new Move(player);
            }
            else {
                // Convert to double because otherwise the division gives 0
                int x = Double.valueOf((double)tileIndex / (double)game.getBoardSize()).intValue();
                int y = tileIndex % game.getBoardSize();
                desiredMove = new Move(x, y, player);
            }

            Move lastMove = player.getLastMove();

            if (lastMove != null && !lastMove.didPlayerPlay() && !desiredMove.didPlayerPlay()) {
                // Player passed twice
                String message = "player " + player.getName() + " has passed twice, thus ending the game";
                gameHandler.gameFinished(message);
            }
            else if (game.legalMove(player.getLastMove(), desiredMove)) {
                // If this move is legal, update the game accordingly
                game.makeMove(desiredMove);
            }
            else {
                // Send invalid move
                System.out.println("[" + game.getId() + "] invalid move wanted, notifying client that this is not allowed");
                out.println(new InvalidMoveCommand("Not allowed to place " + player.getStone().getColorDescription() + " at " + desiredMove.getX() + "," + desiredMove.getY()));
                return;
            }

            // Update player's last move accordingly
            player.setLastMove(desiredMove);

            // Acknowledge move to player
            MoveObject moveObject = new MoveObject(desiredMove.getX(), desiredMove.getY());
            AcknowledgeMoveCommand moveCommand = new AcknowledgeMoveCommand(game.getId(), moveObject, game.getGameState());

            System.out.println("[" + game.getId() + "] " + player.getName() + " places " + player.getStone().getColorDescription() + " at " + desiredMove.getX() + "," + desiredMove.getY() + ": " + moveCommand);
            if (game.isFinished()) {
                gameHandler.gameFinished(GameFinishedCommand.GAME_FINISHED);
            }
            else {
                gameHandler.acknowledgeMove(moveCommand);
            }
        }
    }

    /**
     * Acknowledges given move command (via game handler)
     */
    void acknowledgeMove(AcknowledgeMoveCommand moveCommand) {
        out.println(moveCommand);
    }

    /**
     * Acknowledges that the game finished
     * @param reason the reason why the game finished (normally, player passed twice)
     */
    void acknowledgeGameFinished(String reason) {
        Game game = gameHandler.getGame();

        Player opponent = game.getOpponent(player);
        int myPoints = game.getBoard().areaScore(player.getStone());
        int herPoints = game.getBoard().areaScore(opponent.getStone());

        String winnerName = myPoints > herPoints ? player.getName() : opponent.getName();
        out.println(new GameFinishedCommand(game.getId(), winnerName, game.getScore(), reason));

        gameHandler.stopGame();
    }

    /**
     * Request a rematch
     */
    void requestRematch() {
        out.println(new RequestRematchCommand());
    }
}
