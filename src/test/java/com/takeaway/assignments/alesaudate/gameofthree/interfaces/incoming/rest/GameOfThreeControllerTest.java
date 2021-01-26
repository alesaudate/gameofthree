package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThreeManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.StartGameParamsFixtures.startGameParamsWithInitialValueAndGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.StartGameParamsFixtures.startGameParamsWithInitialValueOnly;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = GameOfThreeController.class)
class GameOfThreeControllerTest {

	@Autowired
	GameOfThreeController gameOfThreeController;

	@MockBean
	GameOfThreeManager gameOfThreeManager;

	@Test
	void testNewGameWithNullParams() {
		gameOfThreeController.newGame(null);
		verify(gameOfThreeManager).newGame(null);
	}

	@Test
	void testNewGameWithInitialValue() {
		var params = startGameParamsWithInitialValueOnly();
		gameOfThreeController.newGame(params);
		verify(gameOfThreeManager).newGame(eq(params.getInitialValue()), eq(null));
	}

	@Test
	void testNewGameWithInitialValueAndGameMode() {
		var params = startGameParamsWithInitialValueAndGameMode();
		gameOfThreeController.newGame(params);
		verify(gameOfThreeManager).newGame(eq(params.getInitialValue()), eq(params.getGameMode()));
	}

}
