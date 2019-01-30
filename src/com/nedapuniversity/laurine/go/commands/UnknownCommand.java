package com.nedapuniversity.laurine.go.commands;

public class UnknownCommand {
    public static final String PLAYER_CANNOT_HAVE_SAME_NAME = "Player cannot have the same name";


    String message;

    public UnknownCommand(String theMessage) {
        message = theMessage;
    }

    @Override
    public String toString() {
        return Command.UNKNOWN_COMMAND+ Command.DELIMITER + message;
    }
}
