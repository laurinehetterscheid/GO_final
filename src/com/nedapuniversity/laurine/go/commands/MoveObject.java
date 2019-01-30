package com.nedapuniversity.laurine.go.commands;

public class MoveObject {

    int index;
    int color;

    public MoveObject(int theIndex, int theColor) {
        index = theIndex;
        color = theColor;
    }

    @Override
    public String toString() {
        return index + Command.OBJECT_DELIMITER + color;
    }
}
