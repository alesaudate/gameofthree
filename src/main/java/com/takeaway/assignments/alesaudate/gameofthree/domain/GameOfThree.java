package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidConfigurationException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidModifierException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;
import java.util.Optional;

/**
 * Game of Three's core class. Contains the rules of the calculations involved, such as
 * what are the permitted modifiers, how to do the calculation of the remaining value, and
 * so on.
 *
 * Designed to be immutable in order to reduce the risk of accidental changes.
 */

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public final class GameOfThree {

	int value;

	GameMode gameMode;

	Role currentRole;

	public static final int[] MODIFIERS = { -1, 0, 1 };

	public static final int MOD = 3;

	public GameOfThree(int value, GameMode gameMode, Role currentRole) {
		this.value = Optional.of(value).filter(x -> x > 1)
				.orElseThrow(() -> new InvalidConfigurationException("Game of three value cannot be less than 1"));
		this.gameMode = Optional.ofNullable(gameMode)
				.orElseThrow(() -> new InvalidConfigurationException("Game of three mode cannot be null"));
		this.currentRole = currentRole;
	}

	public GameData play(int modifierValue) {
		Arrays.stream(MODIFIERS).filter(x -> x == modifierValue).findAny().orElseThrow(InvalidModifierException::new);

		if (modIsZero(modifierValue)) {
			int newValue = calculate(modifierValue);
			return new GameData(modifierValue, newValue, gameMode, currentRole);
		}
		throw new InvalidModifierException();
	}

	private int calculate(int modifier) {
		return (this.value + modifier) / MOD;
	}

	private boolean modIsZero(int modifier) {
		return (this.value + modifier) % MOD == 0;
	}

	public GameData initialGameData() {
		return new GameData(0, this.value, gameMode, null);
	}

}
