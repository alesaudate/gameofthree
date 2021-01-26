package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.github.javafaker.Faker;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.GameStateException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidConfigurationException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidInitialGameValueException;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.kafka.KafkaPublisher;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal.UserInteraction;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.Locale;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameDataFixtures.aboutToWinGameData;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameDataFixtures.finishedGameData;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameDataFixtures.nonWinningGameData;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class GameOfThreeManagerTest {

	@MockBean
	KafkaPublisher kafkaPublisher;

	@MockBean
	UserInteraction userInteraction;

	@MockBean
	HumanPlayer humanPlayer;

	@MockBean
	ComputerPlayer computerPlayer;

	@Autowired
	GameOfThreeManager gameOfThreeManager;

	private static final Faker FAKER = new Faker(new Locale("en"));

	@Test
	void testDoesNotStartGameIfModifierIsTooSmall() {
		var value = RND.nextInt(3);
		assertThrows(InvalidInitialGameValueException.class, () -> gameOfThreeManager.newGame(value, randomGameMode()));
	}

	@Test
	void testDoesNotProceedGameIfGameDataIsNull() {
		assertThrows(GameStateException.class, () -> gameOfThreeManager.proceedGame(null));
	}

	@Test
	void testDoesNotInstantiateManagerIfMaxRandomValueIsTooSmall() {
		assertThrows(InvalidConfigurationException.class, () -> new GameOfThreeManager(RND.nextInt(2), randomGameMode(),
				FAKER.name().firstName(), kafkaPublisher, humanPlayer, computerPlayer, userInteraction));
	}

	@Test
	void testStartNewGameWithProvidedValue() {
		var value = RND.nextInt(Integer.MAX_VALUE - 2) + 2;
		var gameMode = randomGameMode();

		gameOfThreeManager.newGame(value, gameMode);

		var captor = ArgumentCaptor.forClass(PortableGameData.class);
		verify(userInteraction).tellThatTheGameHasBegun(eq("test"), eq(value));
		verify(kafkaPublisher).publish(captor.capture());

		var portableGamaData = captor.getValue();
		assertEquals(value, portableGamaData.getResultingNumber());
		assertEquals(0, portableGamaData.getAdded());
		assertEquals(gameMode.name(), portableGamaData.getGameMode());
	}

	@Test
	void testStartNewGameWithRandomValue() {
		var gameMode = randomGameMode();

		gameOfThreeManager.newGame(gameMode);

		var captor = ArgumentCaptor.forClass(PortableGameData.class);
		verify(userInteraction).tellThatTheGameHasBegun(eq("test"), anyInt());
		verify(kafkaPublisher).publish(captor.capture());

		var portableGamaData = captor.getValue();
		assertTrue(portableGamaData.getResultingNumber() >= 2);
		assertEquals(0, portableGamaData.getAdded());
		assertEquals(gameMode.name(), portableGamaData.getGameMode());
	}

	@Test
	void testStartNewGameWithNullGameMode() {
		gameOfThreeManager.newGame(null);

		var captor = ArgumentCaptor.forClass(PortableGameData.class);
		verify(kafkaPublisher).publish(captor.capture());

		var gameData = captor.getValue();
		assertTrue(gameData.getResultingNumber() >= 2);
		assertEquals(0, gameData.getAdded());
		assertEquals(GameMode.COMPUTER_VS_COMPUTER.name(), gameData.getGameMode());
	}

	@Test
	void testProceedNonWinningGame() {
		var responseGameData = nonWinningGameData();
		var gameData = nonWinningGameData();

		when(humanPlayer.play(any())).thenReturn(responseGameData);
		when(computerPlayer.play(any())).thenReturn(responseGameData);

		gameOfThreeManager.proceedGame(gameData);

		var captor = ArgumentCaptor.forClass(PortableGameData.class);
		verify(kafkaPublisher).publish(captor.capture());
		verify(userInteraction, never()).tellWhichUserWon(any());

		var proceededGameData = captor.getValue();
		assertEquals(responseGameData.getResultingNumber(), proceededGameData.getResultingNumber());
		assertEquals(responseGameData.getGameMode().name(), proceededGameData.getGameMode());
		assertEquals(responseGameData.getCurrentRole().name(), proceededGameData.getCurrentRole());
		assertEquals(responseGameData.getAdded(), proceededGameData.getAdded());

	}

	@Test
	void testProceedWinningGame() {
		var gameData = aboutToWinGameData();
		var responseGameData = finishedGameData();

		when(humanPlayer.play(any())).thenReturn(responseGameData);
		when(computerPlayer.play(any())).thenReturn(responseGameData);

		gameOfThreeManager.proceedGame(gameData);
		verify(kafkaPublisher, never()).publish(any());
		verify(userInteraction, times(1)).tellWhichUserWon(eq("test"));

	}

}
