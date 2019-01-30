package com.nedapuniversity.laurine.go.commands;

public class AcknowledgeRematchCommand {
    int rematch;

    public AcknowledgeRematchCommand(boolean rematchConfirmed) {
        rematch = rematchConfirmed ? 1 : 0;
    }

    @Override
    public String toString() {
        return Command.ACKNOWLEDGE_REMATCH+ Command.DELIMITER + rematch;
    }
}
