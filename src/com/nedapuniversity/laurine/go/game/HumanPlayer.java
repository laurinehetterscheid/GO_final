/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

import java.util.Scanner;

/**
 * @author laurine.hetterscheid
 *
 */
public class HumanPlayer extends ComputerPlayer {
	
	// Used to ask human player for input
	private Scanner userInput;


	/**
	 * @param userName
	 * @param theUserInput
	 * @param playerId
	 */
	public HumanPlayer(String userName, Scanner theUserInput, int playerId) {
		super(userName, playerId);
		userInput = theUserInput;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.nedapuniversity.laurine.go.game.ComputerPlayer#bestMove()
	 */
	@Override
	protected Move bestMove(Board playBoard) {
		// TODO return input van speler
		return new Move(0, 0, this);
	}



	/* (non-Javadoc)
	 * @see com.nedapuniversity.laurine.go.game.ComputerPlayer#playOrPass(com.nedapuniversity.laurine.go.game.Board)
	 */
	@Override
	protected boolean playOrPass(Board playBoard) {
		// TODO Ask player play (true) or pass (false)
		return true;
	}



	public Move showHint(Board playBoard) {
		return super.bestMove(playBoard);
	}


	@Override
	public Move desiredMove(Board playBoard) {
		System.out.println(playBoard.printBoard());
		System.out.println("Where do you want to place your stone? 'x,y', 'pass' to pass this turn, or 'help' to show a suggested move");

		String tileInput = userInput.nextLine();

		if (tileInput.equalsIgnoreCase("help")) {
			Move move = showHint(playBoard);
			System.out.println("Suggested move is " + move.getX() + "," + move.getY());
		}
		else if (tileInput.equalsIgnoreCase("pass")) {
			return new Move(this);
		}

		String[] coordinates = tileInput.split(",");
		if (coordinates.length < 2) {
			System.err.println("Incorrect input '" + tileInput + "', please specify your move with 'x,y'");
			return desiredMove(playBoard);
		}
		int x = Integer.parseInt(coordinates[0]);
		int y = Integer.parseInt(coordinates[1]);
		
		return new Move(x,y,this);
	}
}
