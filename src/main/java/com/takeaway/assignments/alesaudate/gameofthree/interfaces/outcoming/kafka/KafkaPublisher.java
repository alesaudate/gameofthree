package com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.kafka;

import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaPublisher {

	KafkaTemplate<String, PortableGameData> kafkaTemplate;

	String createdGameDataTopic;

	public KafkaPublisher(KafkaTemplate<String, PortableGameData> kafkaTemplate,
			@Value("${kafka.topics.gamedata.created}.${player.destination}") String createdGameDataTopic) {
		this.kafkaTemplate = kafkaTemplate;
		this.createdGameDataTopic = createdGameDataTopic;
	}

	public void publish(PortableGameData gameData) {
		kafkaTemplate.send(createdGameDataTopic, gameData);
	}

}
