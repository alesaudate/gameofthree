package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.kafka;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameData;
import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThreeManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.PortableGameDataFixtures.portableGameDataWithNullRole;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.PortableGameDataFixtures.randomPortableGameData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = KafkaListener.class)
class KafkaListenerTest {

	@Autowired
	private KafkaListener kafkaListener;

	@MockBean
	GameOfThreeManager gameOfThreeManager;

	@Test
	void testListenerHydratesCorrectlyObject() {
		var portableGameData = randomPortableGameData();
		kafkaListener.listen(portableGameData);

		var captor = ArgumentCaptor.forClass(GameData.class);
		verify(gameOfThreeManager).proceedGame(captor.capture());

		var gameData = captor.getValue();
		assertEquals(portableGameData.getGameMode(), gameData.getGameMode().name());
		assertEquals(portableGameData.getAdded(), gameData.getAdded());
		assertEquals(portableGameData.getCurrentRole(), gameData.getCurrentRole().name());
		assertEquals(portableGameData.getResultingNumber(), gameData.getResultingNumber());

	}

	@Test
	void testListnerHydratesObjectWithNullRole() {
		var portableGameData = portableGameDataWithNullRole();
		kafkaListener.listen(portableGameData);

		var captor = ArgumentCaptor.forClass(GameData.class);
		verify(gameOfThreeManager).proceedGame(captor.capture());

		var gameData = captor.getValue();
		assertEquals(portableGameData.getGameMode(), gameData.getGameMode().name());
		assertEquals(portableGameData.getAdded(), gameData.getAdded());
		assertNull(gameData.getCurrentRole());
		assertEquals(portableGameData.getResultingNumber(), gameData.getResultingNumber());
	}

}
