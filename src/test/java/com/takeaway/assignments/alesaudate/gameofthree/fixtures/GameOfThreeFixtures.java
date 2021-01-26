package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThree;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.RoleFixtures.randomRole;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.generateNumberDivisibleByThree;

public class GameOfThreeFixtures {

	public static GameOfThree randomGameOfThree() {
		return new GameOfThree(generateNumberDivisibleByThree(), randomGameMode(), randomRole());
	}

	public static GameOfThree randomGameOfThreeWithNumber(int number) {
		return new GameOfThree(number, randomGameMode(), randomRole());
	}

	public static int randomModifier() {
		return GameOfThree.MODIFIERS[RND.nextInt(GameOfThree.MODIFIERS.length)];
	}

}
