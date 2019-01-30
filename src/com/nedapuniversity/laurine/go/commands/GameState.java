package com.nedapuniversity.laurine.go.commands;

public class GameState {

    public static final String PLAYING = "PLAYING";
    public static final String FINISHED = "FINISHED";

    String status;
    int currentPlayerIndex;
    String board;

    public GameState(String theStatus, int theCurrentPlayer, String theBoard) {
        status = theStatus;
        currentPlayerIndex = theCurrentPlayer;
        board = theBoard;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public static GameState fromString(String input) {
        String[] parts = input.split(Command.OBJECT_DELIMITER);
        if (parts.length != 3) {
            throw new IllegalStateException("Unexpected input for game state '" + input + "'");
        }
        int currentPlayerId = Integer.parseInt(parts[1]);
        return new GameState(parts[0], currentPlayerId, parts[2]);
    }

    @Override
    public String toString() {
        return status + Command.OBJECT_DELIMITER + currentPlayerIndex + Command.OBJECT_DELIMITER + board;
    }
}
