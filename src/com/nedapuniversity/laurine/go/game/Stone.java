/**
 * 
 */
package com.nedapuniversity.laurine.go.game;

/**
 * @author laurine.hetterscheid
 *
 */
public enum Stone {

	EMPTY, 
	BLACK,
	WHITE;

	public int getColorID() {
		if (this == Stone.BLACK) {
			return 1;
		}
		if (this == Stone.WHITE) {
			return 2;
		}
		return 0;
	}

	public String getColorDescription() {
		if (this == Stone.BLACK) {
			return "black";
		}
		if (this == Stone.WHITE) {
			return "white";
		}
		return "empty";
	}
}
