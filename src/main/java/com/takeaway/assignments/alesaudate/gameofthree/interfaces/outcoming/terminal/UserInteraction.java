package com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.Scanner;

@Component
public class UserInteraction {

	private InputStream input;

	private PrintStream out;

	public UserInteraction(@Nullable InputStream input, @Nullable PrintStream out) {
		this.input = Optional.ofNullable(input).orElse(System.in);
		this.out = Optional.ofNullable(out).orElse(System.out);
	}

	public int askUserWhatsTheModifier(int currentValue) {
		Scanner scanner = new Scanner(input); // If closed, then it will close
												// System.in too
		out.printf("The current value is %d, which modifier do you want to apply?%n", currentValue);
		String value = readInput(scanner);
		while (true) {
			try {
				return Integer.parseInt(value);
			}
			catch (NumberFormatException e) {
				out.printf("%s is not a valid int. Please input an integer number%n", value);
				value = readInput(scanner);
			}
		}
	}

	private String readInput(Scanner scanner) {
		var line = scanner.nextLine();
		while (StringUtils.isBlank(line)) {
			line = scanner.nextLine();
		}
		return line.trim();
	}

	public void tellThatTheGameHasBegun(String playerName, int initialValue) {
		out.printf("Player %s has started the game with number %d.%n", playerName, initialValue);
	}

	public void tellUserValueHasJustArrived(int value, String player) {
		out.printf("The value %d is on play. Waiting for %s%n", value, player);
	}

	public void tellUserValueIsAccepted() {
		out.println("The value is accepted.");
	}

	public void tellUserOptionIsIncorrect() {
		out.println("The provided option is not accepted. Use another value.");
	}

	public void tellThatPlayerHasPlayedAndWhatIsTheCurrentData(String playerName, int numberBeforeDivision,
			int modifierApplied, int currentNumber) {
		out.printf("Player %s has played with modifier %d. The new number is (%d + %d) / 3 = %d.%n", playerName,
				modifierApplied, numberBeforeDivision, modifierApplied, currentNumber);
	}

	public void tellWhichUserWon(String userName) {
		out.printf("The user %s has won.%n%n", userName);
	}

}
