package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameData;
import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThree;
import com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.RoleFixtures.randomRole;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;

public class GameDataFixtures {

	public static GameData randomGameData() {
		return new GameData(randomModifier(), MathUtils.generateNumberDivisibleByThree(), randomGameMode(),
				randomRole());
	}

	public static GameData nonWinningGameData() {
		return new GameData(randomModifier(), MathUtils.generateNumberDivisibleByThreeGreaterThan(5), randomGameMode(),
				randomRole());
	}

	public static GameData aboutToWinGameData() {
		var modifier = GameOfThree.MODIFIERS[RND.nextInt(GameOfThree.MODIFIERS.length)];
		return new GameData(randomModifier(), GameOfThree.MOD + modifier, randomGameMode(), randomRole());

	}

	public static GameData finishedGameData() {
		return new GameData(randomModifier(), 1, randomGameMode(), randomRole());
	}

	private static int randomModifier() {
		return GameOfThree.MODIFIERS[RND.nextInt(GameOfThree.MODIFIERS.length)];
	}

}
