/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.nedapuniversity.laurine.go.commands.GameState;
import com.nedapuniversity.laurine.go.commands.Score;

/**
 * Class for playing the GO game
 * 
 * @author laurine.hetterscheid
 *
 */
public class Game {

	/**
	 * Maximum number of players per game.
	 */
	private static int PLAYER_COUNT = 2;

	/**
	 * The players of the game.
	 */
	private List<Player> players = new ArrayList<>(PLAYER_COUNT);
	private int currentPlayerIndex;

	private Board board;
	private String boardState;
	private String gameStatus;
	private int gameId = -1;
	

	/**
	 * Creates a new Game object.
	 * 
	 * @param theGameId the id for this game
	 */
	public Game(int theGameId) {
		gameId = theGameId;
		currentPlayerIndex = 0;
	}


	/**
	 * @return The game id
	 */
	public int getId() {
		return gameId;
	}

	/**
	 * Creates board with given size
	 *
	 * @param dimensionInput the size of the board
	 */
	public void createBoard(int dimensionInput) {
		board = new Board(dimensionInput);
	}

	/**
	 *
	 * @return true if board has been created
	 */
	public boolean isBoardConfigured() {
		return board != null;
	}


	/**
	 * Resets the game. The board is emptied and player[0] becomes the current
	 * player.
	 */
	public void reset() {
		currentPlayerIndex = 0;
		board.reset();
	}

	/**
	 * Game is full when max number of players is reached
	 */
	public boolean isFullWithPlayers() {
		return players.size() == PLAYER_COUNT;
	}

	/**
	 * Adds new player
	 *
	 * @param newPlayer
	 * @return are we leader?
	 */
	public boolean addPlayer(Player newPlayer) {
		if (!players.isEmpty() && players.get(0).getName().equals(newPlayer.getName())) {
			throw new IllegalStateException("Player cannot have the same name");
		}

		if (players.size() < PLAYER_COUNT) {
			players.add(newPlayer);
		}
		return players.size() < PLAYER_COUNT;
	}

	/**
	 * @return The players in this game
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @return The opponent of the provided me
	 */
	public Player getOpponent(Player me) {
		for (Player player : players) {
			if (player != me) {
				return player;
			}
		}
		return null;
	}

	/**
	 * @return player with given id
	 */
	public Player getPlayerById(int id) {
		for (Player player : players) {
			if (player.getId() == id) {
				return player;
			}
		}
		return null;
	}

	/**
	 * @return player with given name
	 */
	public Player getPlayer(String name) {
		for (Player player : players) {
			if (player.getName().equals(name)) {
				return player;
			}
		}
		return null;
	}


	/**
	 * Determines if the move should be allowed
	 *
	 * @param lastMove previous player's move
	 * @param desiredMove move the player wants to make
	 * @return true if desiredMove is a legalMove.
	 */
	public boolean legalMove(Move lastMove, Move desiredMove) {
		// The same move twice is not allowed
		if (lastMove != null && lastMove.equals(desiredMove)) {
			return false;
		}

		// The move must fit on the board
		int x = desiredMove.getX();
		int y = desiredMove.getY();
		if (x < 0 || x >= board.getDimension() || y < 0 || y >= board.getDimension()) {
			return false;
		}

		// The move must be placed on an empty intersection
		if (board.getStone(x,y) != Stone.EMPTY) {
			return false;
		}

		// All checks passed, move is OK
		return true;
	}

	public void makeMove(Move move) {
		board.setStone(move.getX(), move.getY(), move.getPlayer().getStone());

		// TODO: update board with capturing stones

		// Update currentPlayerIndex with the other player
		currentPlayerIndex = move.getPlayer().getId() == 0 ? 1 : 0;
	}

	/**
	 * @return true if the game is finished
	 */
	public boolean isFinished() {
		return board.isFull();
	}

	public Score getScore() {
		return new Score(board.areaScore(Stone.BLACK),board.areaScore(Stone.WHITE));
	}

	/**
	 * @return the board
	 */
	public int getBoardSize() {
		return board.getDimension();
	}
	
	/**
	 * @param theStatus
	 * @param theCurrentPlayer
	 * @param theBoardState
	 */
	public void setGameState(String theStatus, int theCurrentPlayer, String theBoardState) {
		gameStatus = theStatus;
		currentPlayerIndex = theCurrentPlayer;

		board.updateBoard(boardState);
	}
	
	
	/**
	 * @return new GameState
	 */
	public GameState getGameState() {
		return new GameState(gameStatus, currentPlayerIndex, board.boardStatus());
	}
	
	/**
	 * @return the current player's id
	 */
	public int getCurrentPlayerId() {
		return currentPlayerIndex;
	}

	public String getBoardState() {
		return board.boardStatus();
	}

	/**
	 * @return board clone to prevent direct access by client / server
	 */
	public Board getBoard() {
		return board.clone();
	}
}
