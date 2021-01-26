package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.GameStateException;
import org.junit.jupiter.api.Test;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameDataFixtures.finishedGameData;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameDataFixtures.nonWinningGameData;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.RoleFixtures.randomRole;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameDataTest {

	@Test
	void testIsNotFinished() {
		assertFalse(nonWinningGameData().isFinished());
	}

	@Test
	void testIsFinished() {
		assertTrue(finishedGameData().isFinished());
	}

	@Test
	void testDoesNotInstantiateIfResultingNumberIsSmallerThanOne() {
		assertThrows(GameStateException.class,
				() -> new GameData(0, RND.nextInt(Integer.MAX_VALUE) * -1, randomGameMode(), randomRole()));
	}

	@Test
	void testDoesNotInstantiateIfGameModeIsNull() {
		assertThrows(GameStateException.class,
				() -> new GameData(0, RND.nextInt(Integer.MAX_VALUE), null, randomRole()));
	}

}
