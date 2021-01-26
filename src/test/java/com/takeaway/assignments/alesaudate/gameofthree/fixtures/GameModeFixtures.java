package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameMode;

import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;

public class GameModeFixtures {

	public static GameMode randomGameMode() {
		return GameMode.values()[RND.nextInt(GameMode.values().length)];
	}

}
