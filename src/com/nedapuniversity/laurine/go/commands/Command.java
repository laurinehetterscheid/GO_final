package com.nedapuniversity.laurine.go.commands;

public abstract class Command {
    public static final String DELIMITER = "+";
    public static final String ESCAPED_DELIMITER = "\\" + DELIMITER;
    public static final String OBJECT_DELIMITER = ";";

    // Client sends HANDSHAKE, server replies with ACKNOWLEGDE_HANDSHAKE
    public static final String HANDSHAKE = "HANDSHAKE"; //+$PLAYER_NAME
    public static final String ACKNOWLEDGE_HANDSHAKE = "ACKNOWLEGDE_HANDSHAKE"; //+$GAME_ID+$IS_LEADER

    // Server sends REQUEST_CONFIG, client replies with SET_CONFIG, server replies with ACKNOWLEDGE_CONFIG
    public static final String REQUEST_CONFIG = "REQUEST_CONFIG"; //+$REQUEST_CONFIG_MESSAGE
    public static final String SET_CONFIG = "SET_CONFIG"; //+$GAME_ID+$PREFERRED_COLOR+$BOARD_SIZE
    public static final String ACKNOWLEDGE_CONFIG = "ACKNOWLEDGE_CONFIG"; //+$PLAYER_NAME+$COLOR+$SIZE+$GAME_SATE+$OPPONENT

    // Server sends REQUEST_REMATCH, client replies with SET_REMATCH, server replies with ACKNOWLEDGE_REMATCH
    public static final String REQUEST_REMATCH = "REQUEST_REMATCH"; //(no arguments)
    public static final String SET_REMATCH = "SET_REMATCH"; //+$REMATCH
    public static final String ACKNOWLEDGE_REMATCH = "ACKNOWLEDGE_REMATCH"; //+$REMATCH

    // Client sends MOVE, server replies with either ACKNOWLEDGE_MOVE or INVALID_MOVE
    public static final String MOVE = "MOVE"; //+$GAME_ID+$PLAYER_NAME+$TILE_INDEX
    public static final String ACKNOWLEDGE_MOVE = "ACKNOWLEDGE_MOVE"; //+$GAME_ID+$MOVE+$GAME_STATE
    public static final String INVALID_MOVE = "INVALID_MOVE"; //+$ERROR_MESSAGE

    // Client sends EXIT, server replies with GAME_FINISHED
    public static final String EXIT = "EXIT"; //+$GAME_ID+$PLAYER_NAME
    public static final String GAME_FINISHED = "GAME_FINISHED"; //+$GAME_ID+$WINNER+$SCORE+$MESSAGE

    // Server replies with UNKNOWN_COMMAND when client sends a command that isn't recognized in above commands
    public static final String UNKNOWN_COMMAND = "UNKNOWN_COMMAND"; //+$MESSAGE


}
