package com.nedapuniversity.laurine.go.commands;

public class SetRematchCommand {
    int rematch;

    public SetRematchCommand(boolean wantRematch) {
        rematch = wantRematch ? 1 : 0;
    }

    @Override
    public String toString() {
        return Command.SET_REMATCH+ Command.DELIMITER + rematch;
    }
}
