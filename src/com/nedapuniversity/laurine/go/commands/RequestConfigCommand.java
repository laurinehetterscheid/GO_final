package com.nedapuniversity.laurine.go.commands;

public class RequestConfigCommand {
    private static final String CONFIG_MESSAGE = "Please provide a preferred configuration";

    public RequestConfigCommand() {
    }

    @Override
    public String toString() {
        return Command.REQUEST_CONFIG + Command.DELIMITER + CONFIG_MESSAGE;
    }
}
