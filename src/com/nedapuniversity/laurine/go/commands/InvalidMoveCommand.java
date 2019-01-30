package com.nedapuniversity.laurine.go.commands;

public class InvalidMoveCommand {
    String message;

    public InvalidMoveCommand(String theMessage) {
        message = theMessage;
    }

    @Override
    public String toString() {
        return Command.INVALID_MOVE + Command.DELIMITER + message;
    }
}
