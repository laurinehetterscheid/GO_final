package com.nedapuniversity.laurine.go.commands;

public class GameFinishedCommand {

    public static String GAME_FINISHED = "The game has finished";

    int gameId;
    String winner;
    Score score;
    String message;

    public GameFinishedCommand(int theGameId, String theWinnerName, Score theScore, String theMessage) {
        gameId = theGameId;
        winner = theWinnerName;
        score = theScore;
        message = theMessage;
    }

    @Override
    public String toString() {
        return Command.GAME_FINISHED + Command.DELIMITER + gameId + Command.DELIMITER + winner + Command.DELIMITER + score + Command.DELIMITER + message;
    }
}
