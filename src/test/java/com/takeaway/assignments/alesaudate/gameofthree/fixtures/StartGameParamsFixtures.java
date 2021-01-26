package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.request.StartGameParams;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;

public class StartGameParamsFixtures {

	public static StartGameParams startGameParamsWithInitialValueOnly() {
		return new StartGameParams(null, RND.nextInt());
	}

	public static StartGameParams startGameParamsWithInitialValueAndGameMode() {
		return new StartGameParams(randomGameMode(), RND.nextInt());
	}

}
