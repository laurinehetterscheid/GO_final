package com.nedapuniversity.laurine.go.commands;

public class AcknowledgeMoveCommand {
    int gameId;
    MoveObject move;
    GameState gameState;

    public AcknowledgeMoveCommand(int theGameId, MoveObject theMoveObject, GameState theGameState) {
        gameId = theGameId;
        move = theMoveObject;
        gameState = theGameState;
    }

    @Override
    public String toString() {
        return Command.ACKNOWLEDGE_MOVE + Command.DELIMITER + gameId + Command.DELIMITER + move + Command.DELIMITER + gameState;
    }
}
