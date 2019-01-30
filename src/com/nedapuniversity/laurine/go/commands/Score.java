package com.nedapuniversity.laurine.go.commands;

public class Score {

    int pointsBlack;
    int pointsWhite;

    public Score(int thePointsBlack, int thePointsWhite) {
        pointsBlack = thePointsBlack;
        pointsWhite = thePointsWhite;
    }

    @Override
    public String toString() {
        return pointsBlack + Command.OBJECT_DELIMITER + pointsWhite;
    }
}
