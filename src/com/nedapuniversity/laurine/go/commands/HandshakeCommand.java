package com.nedapuniversity.laurine.go.commands;

public class HandshakeCommand {
    String playerName;

    public HandshakeCommand(String thePlayerName) {
        playerName = thePlayerName;
    }

    @Override
    public String toString() {
        return Command.HANDSHAKE + Command.DELIMITER + playerName;
    }
}
