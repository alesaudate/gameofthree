package com.takeaway.assignments.alesaudate.gameofthree.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameModeTest {

	@Test
	void testPlayerVsComputerReturnsCorrectRoleIfNullIsProvided() {
		assertEquals(Role.COMPUTER, GameMode.PLAYER_VS_COMPUTER.switchRole(null));
	}

	@Test
	void testPlayerVsComputerReturnsComputerIfPlayerIsProvided() {
		assertEquals(Role.COMPUTER, GameMode.PLAYER_VS_COMPUTER.switchRole(Role.PLAYER));
	}

	@Test
	void testPlayerVsComputerReturnsPlayerIfComputerIsProvided() {
		assertEquals(Role.PLAYER, GameMode.PLAYER_VS_COMPUTER.switchRole(Role.COMPUTER));
	}

	@Test
	void testPlayerVsPlayerReturnsCorrectRoleIfNullIsProvided() {
		assertEquals(Role.PLAYER, GameMode.PLAYER_VS_PLAYER.switchRole(null));
	}

	@Test
	void testPlayerVsPlayerReturnsPlayerIfPlayerIsProvided() {
		assertEquals(Role.PLAYER, GameMode.PLAYER_VS_PLAYER.switchRole(Role.PLAYER));
	}

	@Test
	void testPlayerVsPlayerReturnsPlayerIfComputerIsProvided() {
		assertEquals(Role.PLAYER, GameMode.PLAYER_VS_PLAYER.switchRole(Role.COMPUTER));
	}

	@Test
	void testComputerVsComputerReturnsCorrectRoleIfNullIsProvided() {
		assertEquals(Role.COMPUTER, GameMode.COMPUTER_VS_COMPUTER.switchRole(null));
	}

	@Test
	void testComputerVsComputerReturnsComputerIfPlayerIsProvided() {
		assertEquals(Role.COMPUTER, GameMode.COMPUTER_VS_COMPUTER.switchRole(Role.PLAYER));
	}

	@Test
	void testComputerVsComputerReturnsComputerIfComputerIsProvided() {
		assertEquals(Role.COMPUTER, GameMode.COMPUTER_VS_COMPUTER.switchRole(Role.COMPUTER));
	}

}
