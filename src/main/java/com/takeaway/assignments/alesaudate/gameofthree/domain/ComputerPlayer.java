package com.takeaway.assignments.alesaudate.gameofthree.domain;

import org.springframework.stereotype.Component;

/**
 * Represents the computer as player. It automatically plays, using a modifier that it
 * guesses.
 */

@Component
public class ComputerPlayer implements Player {

	@Override
	public GameData play(GameOfThree gameOfThree) {
		return gameOfThree.play(figureOutModifier(gameOfThree.getValue()));
	}

	private int figureOutModifier(int currentValue) {
		int sumOfDigits = String.valueOf(currentValue).chars().map(Character::getNumericValue).sum();
		if (sumOfDigits >= 10) {
			return figureOutModifier(sumOfDigits);
		}
		if (sumOfDigits == 2 || sumOfDigits == 5 || sumOfDigits == 8) {
			return 1;
		}
		else if (sumOfDigits == 1 || sumOfDigits == 4 || sumOfDigits == 7) {
			return -1;
		}
		else {
			return 0;
		}
	}

}
