package com.takeaway.assignments.alesaudate.gameofthree.fixtures;

import com.takeaway.assignments.alesaudate.gameofthree.domain.Role;

import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;

public class RoleFixtures {

	public static Role randomRole() {
		return Role.values()[RND.nextInt(Role.values().length)];
	}

}
