package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.GameStateException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidModifierException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Optional;

/**
 * This clsss is designed as a means to snapshot what the game's state currently is and
 * then ease the task of forwarding it to another player.
 *
 * Designed to be immutable.
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public final class GameData {

	int added;

	int resultingNumber;

	GameMode gameMode;

	Role currentRole;

	public GameData(int added, int resultingNumber, GameMode gameMode, Role currentRole) {
		this.added = Arrays.stream(GameOfThree.MODIFIERS).filter(x -> x == added).findAny()
				.orElseThrow(InvalidModifierException::new);
		this.resultingNumber = Optional.of(resultingNumber).filter(x -> x >= 1)
				.orElseThrow(() -> new GameStateException("Resulting number should not be smaller than 1"));
		this.gameMode = Optional.ofNullable(gameMode)
				.orElseThrow(() -> new GameStateException("Game mode should not be null"));
		this.currentRole = currentRole;
	}

	public boolean isFinished() {
		return resultingNumber == 1;
	}

}
