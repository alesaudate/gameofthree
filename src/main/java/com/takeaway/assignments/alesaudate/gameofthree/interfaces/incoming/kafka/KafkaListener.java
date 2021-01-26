package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.kafka;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameData;
import com.takeaway.assignments.alesaudate.gameofthree.domain.GameMode;
import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThreeManager;
import com.takeaway.assignments.alesaudate.gameofthree.domain.Role;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaListener {

	@Autowired
	GameOfThreeManager gameOfThreeManager;

	@org.springframework.kafka.annotation.KafkaListener(topics = "${kafka.topics.gamedata.created}.${player.name}")
	public void listen(PortableGameData gameData) {
		// @formatter:off
		var domainGameData = new GameData(gameData.getAdded(),
				gameData.getResultingNumber(),
				GameMode.valueOf(gameData.getGameMode()),
				Optional.ofNullable(gameData.getCurrentRole()).map(Role::valueOf).orElse(null));
		// @formatter:om
		gameOfThreeManager.proceedGame(domainGameData);
	}

}
