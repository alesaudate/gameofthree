package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThree;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;

import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameModeFixtures.randomGameMode;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.GameOfThreeFixtures.randomModifier;
import static com.takeaway.assignments.alesaudate.gameofthree.fixtures.RoleFixtures.randomRole;
import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;

public class PortableGameDataFixtures {

	public static PortableGameData randomPortableGameData() {
		return new PortableGameData(randomModifier(), RND.nextInt(Integer.MAX_VALUE), randomGameMode().name(),
				randomRole().name());
	}

	public static PortableGameData portableGameDataWithNullRole() {
		return new PortableGameData(randomModifier(), RND.nextInt(Integer.MAX_VALUE), randomGameMode().name(), null);
	}

}
