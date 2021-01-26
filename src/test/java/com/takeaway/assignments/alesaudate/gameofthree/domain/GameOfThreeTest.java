package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidConfigurationException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidModifierException;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.RoleFixtures.randomRole;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameOfThreeTest {

	@Test
	void testGameShouldNotBeInstantiatedIfValueIsLessThanOne() {
		assertThrows(InvalidConfigurationException.class, () -> new GameOfThree(0, randomGameMode(), randomRole()));
	}

	@Test
	void testGameShouldNotBeInstantiatedIfValueIsEqualToOne() {
		assertThrows(InvalidConfigurationException.class, () -> new GameOfThree(1, randomGameMode(), randomRole()));
	}

	@Test
	void testGameShouldBeInstantiatedIfValueIsMoreThanOne() {
		assertDoesNotThrow(() -> new GameOfThree(2, randomGameMode(), randomRole()));
	}

	@Test
	void testGameShouldNotBeInstantiatedIfGameModeIsNull() {
		assertThrows(InvalidConfigurationException.class, () -> new GameOfThree(2, null, randomRole()));
	}

	@Test
	void testGameIsNotPlayedIfInvalidModifierIsProvided() {
		assertThrows(InvalidModifierException.class,
				() -> new GameOfThree(2, randomGameMode(), randomRole()).play(RND.nextInt(Integer.MAX_VALUE - 2) + 2));
	}

	@Test
	void testGameProceedsCorrectly() {
		var initialValue = RND.nextInt(Integer.MAX_VALUE - 2) + 2;
		var modifier = getValidModifier(initialValue);
		var game = new GameOfThree(initialValue, randomGameMode(), randomRole());
		var gameData = game.play(modifier);
		assertNotNull(gameData);
		assertEquals((initialValue + modifier) / GameOfThree.MOD, gameData.getResultingNumber());
		assertEquals(modifier, gameData.getAdded());
		assertEquals(game.getGameMode(), gameData.getGameMode());
		assertEquals(game.getCurrentRole(), gameData.getCurrentRole());
	}

	private int getValidModifier(int currentValue) {
		for (int value : GameOfThree.MODIFIERS) {
			if ((currentValue + value) % GameOfThree.MOD == 0) {
				return value;
			}
		}
		throw new RuntimeException("Test failure");
	}

}
