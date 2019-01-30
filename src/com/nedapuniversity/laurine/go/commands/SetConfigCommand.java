/**
 * 
 */
package com.nedapuniversity.laurine.go.commands;

/**
 * @author laurine.hetterscheid
 *
 */
public class SetConfigCommand {
	int gameId;
    int preferredColor;
    int boardSize;

    public SetConfigCommand(int theGameId, int thePreferredColor, int theBoardSize) {
        gameId = theGameId;
        preferredColor = thePreferredColor;
        boardSize = theBoardSize;
        
    }

    @Override
    public String toString() {
        return Command.SET_CONFIG + Command.DELIMITER + Integer.toString(gameId) + Command.DELIMITER + Integer.toString(preferredColor) + Command.DELIMITER + Integer.toString(boardSize);
    }

	
	

}
