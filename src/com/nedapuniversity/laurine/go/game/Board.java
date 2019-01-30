/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

import com.nedapuniversity.laurine.go.game.Stone;

/**
 * board for the GO game.
 * 
 * @author laurine.hetterscheid
 *
 */
public class Board {
	
	private int dimension;

	private Stone[][] intersections;
	
	/**
	 * Constructs board with given dimension
	 * @param dimensionInput
	 */
	public Board(int dimensionInput) {
		dimension = dimensionInput;
		intersections = new Stone[dimension][dimension];
		reset();
	}

	@Override
	protected Board clone()  {
		Board clone = new Board(dimension);
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				clone.intersections[i][j] = intersections[i][j];
			}
		}
		return clone;
	}


	/**
	 * Generates board status with 0,1,2 for empty,black and white respectively
	 * @return String representation of the board's intersections
	 */
	public String boardStatus() {
		String result = "";
		for (int i = 0; i < intersections.length; i++) {
			for (int j = 0; j < intersections.length; j++) {
				Stone s = intersections[i][j];
				switch (s) {
					case EMPTY:
						result += "0'";
						break;
					case WHITE:
						result += "1";
						break;
					case BLACK:
						result += "2";
						break;
				}
			}
		}
		return result;
	}

	void updateBoard(String boardState) {
		if (boardState == null) {
			return;
		}
		if (boardState.length() < dimension*dimension) {
			throw new IllegalStateException("Provided board state is too short given dimension " + dimension);
		}
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				if (intersections[x][y] == Stone.EMPTY) {
					char c = boardState.charAt(x * dimension + y);
					if (c == '0') {
						intersections[x][y] = Stone.EMPTY;
					}
					else if (c == '1') {
						intersections[x][y] = Stone.BLACK;
					}
					else if (c == '2') {
						intersections[x][y] = Stone.WHITE;
					}
				}
			}
		}
	}

	/**
	 * Are all intersections taken?
	 * @return true if board is full
	 */
	public boolean isFull() {
		for (int x = 0; x < dimension; x++) {
			for (int y = 0; y < dimension; y++) {
				if (intersections[x][y] == Stone.EMPTY) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Sets stone on intersection with coordinates x and y.
	 * 
	 * @param x
	 * @param y
	 * @param stone
	 */
	public void setStone(int x, int y, Stone stone) {
		intersections[x][y] = stone;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return stone on intersection with coordinates x and y.
	 */
	public Stone getStone(int x, int y) {
		return intersections[x][y];
	}
	
	
	/**
	 * @return the dimension
	 */
	public int getDimension() {
		return dimension;
	}

	
	/**
	 * resets the board; creates a board with empty intersections.
	 */
	public void reset() {
		for (int x = 0; x < dimension; x++) {
			for (int y =0; y < dimension; y++) {
				setStone(x, y, Stone.EMPTY);
			}
		}
	}


	/**
	 * Calculates area score for a stone color
	 * @param stone the stone to count
	 * @return the score
	 */
	public int areaScore(Stone stone) {
		// Area score is number of stones + number of captured stones
		int areaScore = 0;
		for (int x = 0; x < dimension; x++) {
			for (int y =0; y < dimension; y++) {
				if (intersections[x][y] == stone) {
					areaScore++;
				}
			}
		}

		// TODO: also add calculated captured (empty) intersections

		return areaScore;
	}



	/**
	 * @return Text representation of the board in 2D
	 */
	public String printBoard() {
		String board = "The board looks like: \n  ";

		// Print numbers
		for (int x =0; x < dimension; x++) {
			board += " " + x;
			board += x < 10 ? "  " : " ";
		}
		board += " -> x\n";

		board += boardLine();

		for (int y = 0; y < dimension; y++) {
			board += y < 10 ? " " + y : y;

			for (int x = 0; x < dimension; x++) {
				Stone s = intersections[x][y];
				if (s == Stone.EMPTY) {
					board += " - "; 
				}
				else if (s == Stone.BLACK) {
					board += " B ";
				}
				else if (s == Stone.WHITE) {
					board += " W ";
				}
				if (x < dimension-1) {
					board += "|";
				}
			}			
			board += "\n";
			board += boardLine();
		}
		return board;
	}

	private String boardLine() {
		String line = "  ";
		for (int x =0; x < dimension; x++) {
			line += "----";
		}

		line += "\n";
		return line;
	}
}
