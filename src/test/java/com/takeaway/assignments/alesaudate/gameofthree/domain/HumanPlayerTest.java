package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal.UserInteraction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameOfThreeFixtures.randomGameOfThree;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.figureOutInvalidModifier;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.figureOutModifier;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class HumanPlayerTest {

	@Autowired
	HumanPlayer humanPlayer;

	@MockBean
	UserInteraction userInteraction;

	@Test
	void testUserProvidesValidModifier() {
		var gameOfThree = randomGameOfThree();
		var modifier = figureOutModifier(gameOfThree.getValue());
		when(userInteraction.askUserWhatsTheModifier(eq(gameOfThree.getValue())))
				.thenReturn(figureOutModifier(gameOfThree.getValue()));
		var gameData = humanPlayer.play(gameOfThree);

		verify(userInteraction, times(1)).tellUserValueIsAccepted();
		assertEquals((gameOfThree.getValue() + modifier) / 3, gameData.getResultingNumber());
		assertEquals(modifier, gameData.getAdded());
		assertEquals(gameOfThree.getGameMode(), gameData.getGameMode());
		assertEquals(gameOfThree.getCurrentRole(), gameData.getCurrentRole());
	}

	@Test
	void testUserProvidesInvalidModifier() {
		var gameOfThree = randomGameOfThree();
		var modifier = figureOutModifier(gameOfThree.getValue());
		var invalidModifier = figureOutInvalidModifier(gameOfThree.getValue());

		var randomNumber = RND.nextInt(50) + 2;
		Integer[] numbers = new Integer[randomNumber];
		for (int i = 0; i < numbers.length - 1; i++) {
			numbers[i] = invalidModifier;
		}
		numbers[numbers.length - 1] = modifier;

		when(userInteraction.askUserWhatsTheModifier(eq(gameOfThree.getValue()))).thenReturn(invalidModifier, numbers);

		var gameData = humanPlayer.play(gameOfThree);

		verify(userInteraction, times(randomNumber)).tellUserOptionIsIncorrect();
		verify(userInteraction, times(1)).tellUserValueIsAccepted();

		assertEquals((gameOfThree.getValue() + modifier) / 3, gameData.getResultingNumber());
		assertEquals(modifier, gameData.getAdded());
		assertEquals(gameOfThree.getGameMode(), gameData.getGameMode());
		assertEquals(gameOfThree.getCurrentRole(), gameData.getCurrentRole());
	}

}
