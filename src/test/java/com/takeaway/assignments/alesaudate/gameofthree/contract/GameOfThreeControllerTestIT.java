package com.takeaway.assignments.alesaudate.gameofthree.contract;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameMode;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;
import io.restassured.RestAssured;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EmbeddedKafka(partitions = 1, topics = { "${kafka.topics.gamedata.created}.${player.destination}" },
		brokerProperties = { "listeners=PLAINTEXT://localhost:29093", "port=29093" })
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameOfThreeControllerTestIT {

	@LocalServerPort
	private Integer port;

	@Value("${kafka.topics.gamedata.created}.${player.destination}")
	private String listeningTopic;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	BlockingQueue<ConsumerRecord<String, PortableGameData>> records;

	KafkaMessageListenerContainer<String, PortableGameData> container;

	@BeforeAll
	void setUp() {
		RestAssured.port = port;
		configureKafkaListener();
	}

	@AfterAll
	void tearDown() {
		container.stop();
	}

	@Test
	void testNewGameIsStartedWithEmptyPayload() throws InterruptedException {

		// @formatter:off
		given()
			.log().all()
			.when()
				.body("{}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.post("/v1/api/game")
			.then()
				.statusCode(202);
		// @formatter:on

		var record = records.poll(5, TimeUnit.SECONDS);

		var portableGameData = record.value();
		assertNotNull(portableGameData);
		assertEquals(0, portableGameData.getAdded());
		assertEquals(GameMode.COMPUTER_VS_COMPUTER.name(), portableGameData.getGameMode());
		assertNull(portableGameData.getCurrentRole());
	}

	@Test
	void testNewGameWithoutPayload() throws InterruptedException {

		// @formatter:off
		given()
			.log().all()
			.when()
				.body("{}")
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.post("/v1/api/game")
			.then()
				.statusCode(202);
		// @formatter:on

		var record = records.poll(5, TimeUnit.SECONDS);

		var portableGameData = record.value();
		assertNotNull(portableGameData);
		assertEquals(0, portableGameData.getAdded());
		assertEquals(GameMode.COMPUTER_VS_COMPUTER.name(), portableGameData.getGameMode());
		assertNull(portableGameData.getCurrentRole());
	}

	@Test
	void testNewGameIsStartedWithInvalidValue() throws IOException {
		// @formatter:off
		var responseMessage = given()
			.log().all()
			.when()
				.body(new ClassPathResource("/requestSamples/invalidRequestValue.json").getInputStream())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.post("/v1/api/game")
			.then()
				.statusCode(400)
			.extract()
				.jsonPath()
				.get("errorMessages[0].message");
		// @formatter:on

		assertEquals("The supplied value to start the game is invalid: 1", responseMessage);
	}

	@Test
	void testNewGamePlayerVsPlayer() throws InterruptedException, IOException {

		// @formatter:off
		given()
			.log().all()
			.when()
				.body(new ClassPathResource("/requestSamples/playerToPlayerRequest.json").getInputStream())
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.post("/v1/api/game")
			.then()
				.statusCode(202);
		// @formatter:on

		var record = records.poll(5, TimeUnit.SECONDS);

		var portableGameData = record.value();
		assertNotNull(portableGameData);
		assertEquals(0, portableGameData.getAdded());
		assertEquals(GameMode.PLAYER_VS_PLAYER.name(), portableGameData.getGameMode());
		assertNull(portableGameData.getCurrentRole());
	}

	private void configureKafkaListener() {
		var configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
		var jsonDeserializer = new JsonDeserializer<PortableGameData>();
		jsonDeserializer.addTrustedPackages("*");
		var consumerFactory = new DefaultKafkaConsumerFactory<String, PortableGameData>(configs,
				new StringDeserializer(), jsonDeserializer);
		ContainerProperties containerProperties = new ContainerProperties(listeningTopic);
		container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
		records = new LinkedBlockingQueue<>();
		container.setupMessageListener((MessageListener<String, PortableGameData>) records::add);
		container.start();
		ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
	}

}
