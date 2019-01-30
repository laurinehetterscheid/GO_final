package com.nedapuniversity.laurine.go.server;

import com.nedapuniversity.laurine.go.commands.AcknowledgeMoveCommand;
import com.nedapuniversity.laurine.go.commands.GameFinishedCommand;
import com.nedapuniversity.laurine.go.game.Game;
import com.nedapuniversity.laurine.go.game.Player;
import com.nedapuniversity.laurine.go.game.Stone;

public class GameHandler {

    private ClientHandler leader;
    private ClientHandler opponent;
    private Game game;

    /**
     * Creates a game handler that is responsible for maintaining the connections to both players
     *
     * @param theLeader the first player that joined is 'leader' and has to provide the config
     */
    GameHandler(ClientHandler theLeader) {
        leader = theLeader;
        leader.setHandler(this);
        leader.start();
    }

    /**
     * Adds the opponent
     * @param theOpponent the opponent player
     */
    void addOpponent(ClientHandler theOpponent) {
        opponent = theOpponent;
        opponent.setHandler(this);
        opponent.start();
    }

    /**
     * @return true when both players have joined
     */
    boolean isFull() {
        return leader != null && opponent != null;
    }

    /**
     * Stops the game and disconnects both players
     */
    void stopGame() {
        leader.disconnect();
        opponent.disconnect();
    }

    /**
     * Starts a new game after the initial handshake was acknowledged
     * @param gameId
     */
    void startGame(int gameId) {
        game = new Game(gameId);
    }

    /**
     * @return the game in which both clients are playing
     */
    public Game getGame() {
        return game;
    }

    /**
     * Called when both players are connected, have the handlers notify the clients of the game's config
     */
    void acknowledgeConfiguration() {
        // First ensure that both players have their correct color
        Player player0 = game.getPlayers().get(0);
        Player player1 = game.getPlayers().get(1);
        player1.setStone(player0.getStone() == Stone.BLACK ? 2 : 1);

        leader.acknowledgeConfig();
        opponent.acknowledgeConfig();
    }

    /**
     * Called when a player makes a move, both players are notified via their respective connections
     * @param moveCommand The move
     */
    void acknowledgeMove(AcknowledgeMoveCommand moveCommand) {
        leader.acknowledgeMove(moveCommand);
        opponent.acknowledgeMove(moveCommand);
    }

    /**
     * Notify both clients that the game has finished
     */
    void gameFinished(String message) {
        leader.acknowledgeGameFinished(message);
        opponent.acknowledgeGameFinished(message);

        leader.requestRematch();
        opponent.requestRematch();
    }
}
