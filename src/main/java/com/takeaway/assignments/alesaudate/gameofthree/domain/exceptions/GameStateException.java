package com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions;

/**
 * Represents an exception that occurs when the game tries to enter into an invalid state.
 * Should not generally occur and, if it does, it's most likely representing a bug.
 */
public class GameStateException extends RuntimeException {

	public GameStateException(String message) {
		super(message);
	}

}
