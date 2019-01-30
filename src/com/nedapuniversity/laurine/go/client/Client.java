/**
 * 
 */
package com.nedapuniversity.laurine.go.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import com.nedapuniversity.laurine.go.commands.*;
import com.nedapuniversity.laurine.go.game.*;

/**
 * @author laurine.hetterscheid
 *
 */
public class Client extends Thread {
	private String playerName;
	private Socket socket;
	private Scanner userInput;
	private boolean running = true;

	// For receiving responses from the server
	private BufferedReader in;

	// For sending a command to the server
	private PrintWriter out;

	private boolean isLeader;

	private String playerType;
	private Player player;
	private Game game;

	/**
	 * Creates new Client with a given socket connection
	 * @param thePlayerName The provided player name
	 * @param theSocket The established connection to the server
	 * @param thePlayerType AI or human as provided when started the program
	 * @throws IOException
	 */
	public Client(String thePlayerName, Socket theSocket, String thePlayerType) throws IOException {
		playerName = thePlayerName;
		socket = theSocket;
		playerType = thePlayerType;

		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		userInput = new Scanner(System.in);
	}

	/**
	 * Starts client thread
	 */
	@Override
	public synchronized void run() {
		// TODO does this need to be synchronized?

		// Send HANDSHAKE to register to server
		out.println(new HandshakeCommand(playerName));

		while (running) {
			// Wait for server input before we do anything
			try {
				processServerCommand(in.readLine());
			} catch (IOException exception) {
				System.err.println("Error when listening to server input " + exception.getMessage());
				System.exit(2);
			}
		}

		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException exception) {
			System.err.println("Failed to close socket connection after stopping " + exception.getMessage());
			System.exit(2);
		}

		// All finished now
		System.out.println("Thanks for playing GO!!!");
	}

	/**
	 * Processes server commands and passes recognized command on to dedicated method for further handling
	 */
	private void processServerCommand(String input) {
		System.out.println("Processing incoming server command '" + input + "'");

		String[] command = input.split(Command.ESCAPED_DELIMITER);
		if (command.length == 0) {
			// Server send unexpected command, unable to recover
			throw new IllegalStateException("Unexpected input from server: " + input);
		}

		switch (command[0]) {
		case Command.ACKNOWLEDGE_HANDSHAKE:
			System.out.println("Processing acked handshake '" + input + "'");
			processAcknowledgedHandshake(command);
			break;
		case Command.REQUEST_CONFIG:
			System.out.println("[" + game.getId() + "] Processing request for config '" + input + "'");
			processRequestConfig(command);
			break;
		case Command.ACKNOWLEDGE_CONFIG:
			System.out.println("[" + game.getId() + "] Processing acked config '" + input + "'");
			processAcknowledgedConfig(command);
			break;
		case Command.REQUEST_REMATCH:
			System.out.println("[" + game.getId() + "] Processing request for rematch '" + input + "'");
			processRequestedRematch(command);
			break;
		case Command.ACKNOWLEDGE_REMATCH:
			processAcknowledgedRematch(command);
			break;
		case Command.ACKNOWLEDGE_MOVE:
			System.out.println("[" + game.getId() + "] Processing acked move '" + input + "'");
			processAcknowledgedMove(command);
			break;
		case Command.INVALID_MOVE:
			System.out.println("[" + game.getId() + "] Processing invalid move '" + input + "'");
			processInvalidMove(command);
			break;
		case Command.GAME_FINISHED:
			System.out.println("[" + game.getId() + "] Processing game finished '" + input + "'");
			processGameFinished(command);
			break;
		case Command.UNKNOWN_COMMAND:
			processUnknownCommand(command);
			break;

		default:
			throw new IllegalStateException("Did not recognize server command: " + command[0]);
		}
	}

	private void processAcknowledgedHandshake(String[] command) {
		// Should have 2 args: $GAME_ID and $IS_LEADER

		int gameId = Integer.parseInt(command[1]);
		game = new Game(gameId);

		isLeader = Boolean.parseBoolean(command[2]);
	}


	private void processRequestConfig(String[] command) {
		// Should have 1 args: $REQUEST_CONFIG_MESSAGE

		String configMessage = command[1];

		System.out.println("Server message: " + configMessage);
		System.out.println("What is your preferred color? (Enter 'B' for Black, 'W' for White)");

		String colorInput = userInput.nextLine();
		int preferredColor = 0;

		if (colorInput.equalsIgnoreCase("B")) {
			preferredColor = 1;
		} else if (colorInput.equalsIgnoreCase("W")) {
			preferredColor = 2;
		} else {
			System.out.println("Did not recognize preferred color, color will be assigned randomly");
		}

		int boardDimension = askUserForBoardDimension();

		out.println(new SetConfigCommand(game.getId(), preferredColor, boardDimension));
		
		System.out.println("[" + game.getId() + "] Configuration sent, you might have to wait for another player to join");
	}


	private int askUserForBoardDimension() {
		System.out.println("What size should your board be? (Give a number between 5 and 19)");

		String input = userInput.nextLine();
		try {
			int dimensionInput = Integer.parseInt(input);
			if (dimensionInput <= 19 && dimensionInput >= 5) {
				return dimensionInput;
			} else {
				System.out.println("Wrong input");
			}
		} catch (NumberFormatException exception) {
			System.err.println("Boardsize is not a number '" + input + "'");
		}
		// Failed to get a good answer, asking again
		return askUserForBoardDimension();
	}


	private void processAcknowledgedConfig(String[] command) {
		// Should have 4 args: +$PLAYER_NAME+$COLOR+$SIZE+$GAME_STATE+$OPPONENT

		if (command[1].equals(playerName)) {
			int playerId = isLeader ? 0 : 1;
			if (Player.HUMAN.equals(playerType)) {
				player = new HumanPlayer(command[1], userInput, playerId);
			} else if (Player.AI.equals(playerType)) {
				player = new ComputerPlayer(command[1], playerId);
			}

			int colorId = Integer.parseInt(command[2]);
			player.setStone(colorId);
			game.addPlayer(player);

			int opponentId = isLeader ? 1 : 0;
			Player opponent = new Player(command[5], opponentId);
			opponent.setStone(player.getStone() == Stone.BLACK ? 2 : 1);
			game.addPlayer(opponent);

			// assumption: at start of the game board is always empty and currentPlayerIndex
			// = 0 (boardstate is not used)
			int boardSize = Integer.parseInt(command[3]);
			game.createBoard(boardSize);

			// TODO: implement GUI --> does not work.
			// new Go(boardSize);

			processGameState(command[4]);

			String color = player.getStone().getColorDescription();
			System.out.println("[" + game.getId() + "] Received game configuration, you are player " + player.getId() + ", playing with color " + color + " against " + opponent.getName() + " in game " + game.getId());

			sendDesiredMove();
		}

		else {
			System.err.println("Player name is not recognized " + command);
		}
	}

	private void sendDesiredMove() {
		Move desiredMove = null;
		if (game.getCurrentPlayerId() == player.getId()) {

			desiredMove = player.desiredMove(game.getBoard());

			// Calculate the tile index in 1D from our 2D array
			int tileIndex = (desiredMove.getX() * game.getBoardSize()) + desiredMove.getY();

			System.out.println("[" + game.getId() + "] Sending new move command for " + playerName + ": " + tileIndex);
			out.println(new MoveCommand(game.getId(), playerName, tileIndex));
		}
	}

	private void processGameState(String gameState) {
		System.out.println("[" + game.getId() + "] Processing game state input '" + gameState + "'");

		String[] state = gameState.split(Command.OBJECT_DELIMITER);
		if (state.length < 3) {
			// Server send unexpected gamestate, unable to recover
			throw new IllegalStateException("Unexpected input from server: " + gameState);
		}

		String gameStatus = state[0];
		int currentPlayer = Integer.parseInt(state[1]);
		String boardStatus = state[2];

		game.setGameState(gameStatus, currentPlayer, boardStatus);

	}

	private void processRequestedRematch(String[] command) {
		// Should have 0 args

		System.out.println("Do you want to start a rematch? (Y/N)");

		String input = userInput.nextLine();

		boolean wantsRematch = input.equals("Y");
		out.println(new SetRematchCommand(wantsRematch));
	}

	private void processAcknowledgedRematch(String[] command) {
		// Should have 1 arg: +$REMATCH
		// $REMATCH: int. 1 if you would like a rematch, 0 if you would not.

		if (command[1].equals("1")) {
			System.out.println("[" + game.getId() + "] Rematch request received, waiting for opponent to do so");
		}

		game.reset();

		// after clearing the game we await a new ACKNOWLEDGE_CONFIG
	}

	private void processAcknowledgedMove(String[] command) {
		// Should have 3 args: +$GAME_ID+$MOVE+$GAME_STATE

		int gameId = Integer.parseInt(command[1]);
		if (gameId != game.getId()) {
			System.err.println("[" + game.getId() + "] received and ignoring move for other game with id " + gameId);
			return;
		}

		String[] move = command[2].split(Command.OBJECT_DELIMITER);
		int x = Integer.parseInt(move[0]);
		int y = Integer.parseInt(move[1]);

		// Get player whose turn it *was* and whose turn it *is next* from game state
		GameState gameState = GameState.fromString(command[3]);

		System.out.println("[" + game.getId() + "] move (" + x + "," + y + "), player = " + gameState.getCurrentPlayerIndex() + " from " + gameState);

		int nextPlayerIndex = gameState.getCurrentPlayerIndex();
		Player nextPlayer = game.getPlayerById(nextPlayerIndex);

		int lastPlayerIndex = game.getCurrentPlayerId();
		Player lastPlayer = game.getPlayerById(lastPlayerIndex);

		// Player last in turn makes this move
		game.makeMove(new Move(x,y, lastPlayer));

		System.out.println("[" + game.getId() + "] " + lastPlayer.getName() + " placed stone " + lastPlayer.getStone().getColorDescription() + " at " + x + "," + y);
		System.out.println("[" + game.getId() + "] next player is " + nextPlayer.getName());

		// If current player now is us, then ask player for desired move
		if (nextPlayer == player) {
			sendDesiredMove();
		}
	}


	private void processInvalidMove(String[] command) {
		// Should have 1 arg: +$ERROR_MESSAGE

		System.out.println("Server responded with error: " + command[1]);

	}

	private void processGameFinished(String[] command) {
		// Should have 4 args: +$GAME_ID+$WINNER+$SCORE+$MESSAGE
		System.out.println("[" + game.getId() + "] " + " game has finished because: " + command[4]);
		System.out.println("[" + game.getId() + "] " + " the winner is " + command[2] + " with " + command[3] + " point difference");
	}

	private void processUnknownCommand(String[] command) {
		// Should have 1 arg: +$MESSAGE

		System.out.println("Server responded with error: " + command[1]);

		if (UnknownCommand.PLAYER_CANNOT_HAVE_SAME_NAME.equals(command[1])) {
			System.err.println("Server disallowed us joining this game with name '" + playerName + "', the opponent already chose this name");
			running = false;
		}
	}
}
