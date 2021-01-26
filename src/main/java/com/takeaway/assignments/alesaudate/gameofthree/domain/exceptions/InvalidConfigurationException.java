package com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions;

/**
 * Represents an exception that occurs when there's something wrong with the game's
 * configuration (can be thrown at game's launch or at some point where it's expected only
 * the game engine itself has access). Should not generally occur and, if it does, it's
 * most likely pointing to a bug.
 */
public class InvalidConfigurationException extends RuntimeException {

	public InvalidConfigurationException(String message) {
		super(message);
	}

}
