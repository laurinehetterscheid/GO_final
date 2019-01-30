package com.nedapuniversity.laurine.go.commands;

public class ExitCommand {
    int gameId;
    String playerName;

    public ExitCommand(int theGameId, String thePlayerName) {
        gameId = theGameId;
        playerName = thePlayerName;
    }

    @Override
    public String toString() {
        return Command.EXIT + Command.DELIMITER + gameId + Command.DELIMITER + playerName;
    }
}
