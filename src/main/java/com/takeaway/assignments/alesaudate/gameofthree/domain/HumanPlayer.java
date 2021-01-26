package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidModifierException;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal.UserInteraction;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Represents a human as player. It accepts inputs from the player, tries to play with
 * them and feedbacks the user, telling him / her whether the modifier is accepted or not.
 */

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
public class HumanPlayer implements Player {

	UserInteraction userInteraction;

	public GameData play(GameOfThree gameOfThree) {

		while (true) {
			try {
				int modifier = userInteraction.askUserWhatsTheModifier(gameOfThree.getValue());
				var gameData = gameOfThree.play(modifier);
				userInteraction.tellUserValueIsAccepted();
				return gameData;
			}
			catch (InvalidModifierException e) {
				userInteraction.tellUserOptionIsIncorrect();
			}
		}
	}

}