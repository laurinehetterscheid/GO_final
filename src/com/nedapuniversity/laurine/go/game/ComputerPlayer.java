/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author laurine.hetterscheid
 *
 */
public class ComputerPlayer extends Player {


	/**
	 * @param userName
	 * @param playerId
	 */
	public ComputerPlayer(String userName, int playerId) {
		super(userName, playerId);
	}

	/**
	 * Determines the best MoveObject on playBoard.
	 * 
	 * @param playBoard
	 * @return the best MoveObject
	 */
	protected Move bestMove(Board playBoard) {

		int dimension = playBoard.getDimension();
		List<Intersection> collectionOfEmptyIntersections = new ArrayList<Intersection>(dimension*dimension);

		if (playOrPass(playBoard) == true) {

			for (int x = 0; x < dimension; x++) {
				for (int y =0; y < dimension; y++) {
					if (playBoard.getStone(x, y) == Stone.EMPTY) {
						
						collectionOfEmptyIntersections.add(new Intersection(x,y));
					}
				}
			}
		}

		Random rand = new Random();
		int emptyIndex = rand.nextInt(collectionOfEmptyIntersections.size());
		Intersection emptyIntersection = collectionOfEmptyIntersections.get(emptyIndex);

		int x = emptyIntersection.x;
		int y = emptyIntersection.y;

		return new Move(x, y, this);

		// TODO check de domme class met intersections (Intersection heeft public int x, y)

	}

	/**
	 * Determines if Computerplayer should play or pass.
	 * 
	 * @param playBoard
	 * @return true if Computerplayer should play, false if ComputerPlayer should pass.
	 */
	protected boolean playOrPass(Board playBoard) {
		// TODO: VERY BASIC, But does this even work??
		if (!playBoard.boardStatus().contains("0")) {
			return false;
		}
		return true;
	}

	/**
	 * Gives the desired move on the playboard.
	 * 
	 * @return the desired move.
	 */
	@Override
	public Move desiredMove(Board playBoard) {
		return bestMove(playBoard);
	}
}
