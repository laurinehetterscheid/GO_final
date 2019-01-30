package com.nedapuniversity.laurine.go.commands;

public class AcknowledgeConfigCommand {
    String playerName;
    int color;
    int boardSize;
    String gameState;
    String opponent;

    public AcknowledgeConfigCommand(String thePlayerName, int theColor, int theBoardSize, String theGameState, String theOpponent) {
        playerName = thePlayerName;
        color = theColor;
        boardSize = theBoardSize;
        gameState = theGameState;
        opponent = theOpponent;
    }

    @Override
    public String toString() {
        return Command.ACKNOWLEDGE_CONFIG + Command.DELIMITER + playerName + Command.DELIMITER + color + Command.DELIMITER + boardSize + Command.DELIMITER + gameState + Command.DELIMITER + opponent;
    }
}
