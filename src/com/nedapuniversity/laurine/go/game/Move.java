/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

/**
 * @author laurine.hetterscheid
 *
 */
public class Move {
	private int x;
	private int y;
	private Player player;
	private boolean didPlayerPlay;
	
	/**
	 * @param xInput
	 * @param yInput
	 * @param playerInput
	 */
	public Move(int xInput, int yInput, Player playerInput) {
		x = xInput;
		y = yInput;
		player = playerInput;
		didPlayerPlay = true;
	}
	
	/**
	 * MoveObject that was passed by the player
	 * 
	 * @param playerThatPassedMove The player that passed this move
	 */
	public Move(Player playerThatPassedMove) {
		player = playerThatPassedMove;
		didPlayerPlay = false;
	}
	
	/**
	 * @return true if Player played, false if Player passed.
	 */
	public boolean didPlayerPlay() {
		return didPlayerPlay;
	}
	
	/**
	 * @return x
	 * 
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return y
	 * 
	 */
	public int getY() {
		return  y;		
	}
	
	/**
	 * @return player
	 */
	public Player getPlayer() {
		return player;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Move)) {
			return false;
		}

		Move other = (Move)obj;
		if (other.player != player) { 
			return false;
		}

		// Check if desired move doesn't put a stone on the same x,y
		return other.x == x && other.y == y;
	}
}
