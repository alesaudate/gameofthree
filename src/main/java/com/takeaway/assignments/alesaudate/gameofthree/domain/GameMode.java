package com.takeaway.assignments.alesaudate.gameofthree.domain;

/**
 * Marks the type of the game mode (human vs computer, human vs human or computer vs
 * computer). It is able to switch the roles based on what the current role is.
 */
public enum GameMode {

	PLAYER_VS_COMPUTER {
		@Override
		public Role switchRole(Role currentRole) {
			if (currentRole == Role.COMPUTER) {
				return Role.PLAYER;
			}
			return Role.COMPUTER;
		}
	},
	PLAYER_VS_PLAYER {
		@Override
		public Role switchRole(Role currentRole) {
			return Role.PLAYER;
		}
	},
	COMPUTER_VS_COMPUTER {
		@Override
		public Role switchRole(Role currentRole) {
			return Role.COMPUTER;
		}
	};

	public abstract Role switchRole(Role currentRole);

}
