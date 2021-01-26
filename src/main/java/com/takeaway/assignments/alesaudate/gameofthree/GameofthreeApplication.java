package com.takeaway.assignments.alesaudate.gameofthree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameofthreeApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(GameofthreeApplication.class, args);
		var playerName = context.getEnvironment().getProperty("player.name");
		System.out.printf("Application has started for player %s%n", playerName);
	}

}
