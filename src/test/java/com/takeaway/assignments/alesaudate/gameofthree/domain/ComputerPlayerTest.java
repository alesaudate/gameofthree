package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameOfThreeFixtures;
import com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ComputerPlayer.class)
class ComputerPlayerTest {

	@Autowired
	ComputerPlayer computerPlayer;

	@Test
	void testModifierIsGuessedCorrectly() {

		var gameOfThree = GameOfThreeFixtures.randomGameOfThree();
		var gameData = computerPlayer.play(gameOfThree);
		assertEquals(MathUtils.figureOutModifier(gameOfThree.getValue()), gameData.getAdded());
	}

	@ParameterizedTest
	@ValueSource(ints = { 2, 5, 8 })
	void testModifierPlayedShouldBeOne(int number) {
		var gameOfThree = GameOfThreeFixtures.randomGameOfThreeWithNumber(number);
		var gameData = computerPlayer.play(gameOfThree);
		assertEquals(1, gameData.getAdded());
	}

	@ParameterizedTest
	@ValueSource(ints = { 3, 6, 9 })
	void testModifierPlayedShouldBeZero(int number) {
		var gameOfThree = GameOfThreeFixtures.randomGameOfThreeWithNumber(number);
		var gameData = computerPlayer.play(gameOfThree);
		assertEquals(0, gameData.getAdded());
	}

	@ParameterizedTest
	@ValueSource(ints = { 4, 7, 10 })
	void testModifierPlayedShouldBeMinusOne(int number) {
		var gameOfThree = GameOfThreeFixtures.randomGameOfThreeWithNumber(number);
		var gameData = computerPlayer.play(gameOfThree);
		assertEquals(-1, gameData.getAdded());
	}

}
