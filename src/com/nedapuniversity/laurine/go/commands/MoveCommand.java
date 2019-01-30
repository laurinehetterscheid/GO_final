package com.nedapuniversity.laurine.go.commands;

public class MoveCommand {
    int gameId;
    String playerName;
    int tileIndex;

    public MoveCommand(int theGameId, String thePlayerName, int theTileIndex) {
        gameId = theGameId;
        playerName = thePlayerName;
        tileIndex = theTileIndex;
    }

    @Override
    public String toString() {
        return Command.MOVE + Command.DELIMITER + gameId + Command.DELIMITER + playerName + Command.DELIMITER + tileIndex;
    }
}
