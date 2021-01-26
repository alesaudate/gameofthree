package com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

import static com.takeaway.assignments.alesaudate.gameofthree.utils.MathUtils.RND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserInteractionTest {

	private static final Faker FAKER = new Faker(new Locale("en"));

	@Test
	void testAskUserWhatsTheModifier() {

		var input = new ByteArrayInputStream("\n\n\ntest\n\n1\n".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var userInteraction = new UserInteraction(input, output);
		var response = userInteraction.askUserWhatsTheModifier(RND.nextInt());

		assertEquals(1, response);
		var printedString = baos.toString();
		assertTrue(printedString.contains("test is not a valid int. Please input an integer number"));
	}

	@Test
	void testTellUserValueHasJustArrived() {
		var input = new ByteArrayInputStream("".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var value = RND.nextInt();
		var name = FAKER.name().firstName();

		var userInteraction = new UserInteraction(input, output);
		userInteraction.tellUserValueHasJustArrived(value, name);

		var printedString = baos.toString();
		assertTrue(printedString.contains(String.format("The value %d is on play. Waiting for %s%n", value, name)));
	}

	@Test
	void testTellUserValueIsAccepted() {
		var input = new ByteArrayInputStream("".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var userInteraction = new UserInteraction(input, output);
		userInteraction.tellUserValueIsAccepted();

		var printedString = baos.toString();
		assertTrue(printedString.contains("The value is accepted."));
	}

	@Test
	void testTellUserOptionIsIncorrect() {
		var input = new ByteArrayInputStream("".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var userInteraction = new UserInteraction(input, output);
		userInteraction.tellUserOptionIsIncorrect();

		var printedString = baos.toString();
		assertTrue(printedString.contains("The provided option is not accepted. Use another value."));
	}

	@Test
	void testTellWhichUserWon() {
		var input = new ByteArrayInputStream("".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var value = RND.nextInt();
		var name = FAKER.name().firstName();

		var userInteraction = new UserInteraction(input, output);
		userInteraction.tellWhichUserWon(name);

		var printedString = baos.toString();
		assertTrue(printedString.contains(String.format("The user %s has won.%n%n", name)));
	}

	@Test
	void testTellThatPlayerHasPlayedAndWhatIsTheCurrentData() {
		var input = new ByteArrayInputStream("".getBytes());
		var baos = new ByteArrayOutputStream();
		var output = new PrintStream(baos);

		var numberBeforeDivision = RND.nextInt();
		var modifierApplied = RND.nextInt();
		var currentNumber = RND.nextInt();
		var name = FAKER.name().firstName();

		var userInteraction = new UserInteraction(input, output);
		userInteraction.tellThatPlayerHasPlayedAndWhatIsTheCurrentData(name, numberBeforeDivision, modifierApplied,
				currentNumber);

		var printedString = baos.toString();
		assertTrue(printedString.contains(
				String.format("Player %s has played with modifier %d. The new number is (%d + %d) / 3 = %d.%n", name,
						modifierApplied, numberBeforeDivision, modifierApplied, currentNumber)));
	}

}
