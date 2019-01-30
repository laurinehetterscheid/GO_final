/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laurine.hetterscheid
 *
 */
public class Player {

	/**
	 * Playertype human
	 */
	public static final String HUMAN = "human";
	
	/**
	 * Playertype ai
	 */
	public static final String AI = "ai";
	
	private String playerName;
	
	private Stone myColor = Stone.EMPTY;

	private int playerId;

	private Move lastMove = null;



	/**
	 * Constructs a Player
	 * 
	 * @param thePlayerName
	 */
	public Player(String thePlayerName, int thePlayerId) {
		playerName = thePlayerName;
		playerId = thePlayerId;
	}


	/**
	 * 
	 * @param playBoard
	 * @return move
	 */
	public Move desiredMove(Board playBoard) {
		return null;
	}
	
	public void setStone(int colorIndex) {
		if (colorIndex <= 1) {
			myColor = Stone.BLACK;
		}
		if (colorIndex == 2) {
			myColor = Stone.WHITE;
		}
	}
	/**
	 * 
	 * @return stone
	 */
	public Stone getStone() { 
		return myColor;
	}
	
	
	/**
	 * 
	 * @return name
	 */
	public String getName() { 
		return playerName;
	}


	public int getId() {
		return playerId;
	}

	public Move getLastMove() {
		return lastMove;
	}

	public void setLastMove(Move lastMove) {
		this.lastMove = lastMove;
	}
}
