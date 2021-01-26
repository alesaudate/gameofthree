package com.takeaway.assignments.alesaudate.gameofthree.utils;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThree;

import java.util.Random;

public class MathUtils {

	public static final Random RND = new Random();

	public static int generateNumberDivisibleByThree() {
		return generateNumberDivisibleByThreeGreaterThan(0);
	}

	public static int generateNumberDivisibleByThreeGreaterThan(int value) {
		int number = RND.nextInt(Integer.MAX_VALUE - value) + value;
		int modifier = figureOutModifier(number);
		return number + modifier;
	}

	public static int figureOutModifier(int currentValue) {
		for (int value : GameOfThree.MODIFIERS) {
			if ((currentValue + value) % GameOfThree.MOD == 0) {
				return value;
			}
		}
		throw new RuntimeException("Could not figure out modifier");
	}

	public static int figureOutInvalidModifier(int currentValue) {
		var modifier = figureOutModifier(currentValue);
		if (modifier == 1 || modifier == -1) {
			return modifier * -1;
		}
		return modifier + 1;
	}

}
