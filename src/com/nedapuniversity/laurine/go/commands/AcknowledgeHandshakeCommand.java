package com.nedapuniversity.laurine.go.commands;

public class AcknowledgeHandshakeCommand {
    int gameId;
    boolean isLeader;

    public AcknowledgeHandshakeCommand(int theGameId, boolean isTheLeader) {
        gameId = theGameId;
        isLeader = isTheLeader;
    }

    @Override
    public String toString() {
        return Command.ACKNOWLEDGE_HANDSHAKE + Command.DELIMITER + Integer.toString(gameId) + Command.DELIMITER + Boolean.toString(isLeader);
    }
}
