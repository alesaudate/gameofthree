package com.takeaway.assignments.alesaudate.gameofthree.domain;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.GameStateException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidConfigurationException;
import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidInitialGameValueException;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.exchange.PortableGameData;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.kafka.KafkaPublisher;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.outcoming.terminal.UserInteraction;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Optional;
import java.util.Random;

/**
 * Manages the development of the game and its states. Therefore, it is responsible for
 * starting a new game, warning each player when it's time to play, and so on.
 */

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GameOfThreeManager {

	Random random;

	int maxRandomValue;

	GameMode defaultGameMode;

	String currentPlayerName;

	KafkaPublisher kafkaPublisher;

	UserInteraction userInteraction;

	EnumMap<Role, Player> playersAccordingToRoles;

	// @formatter:off
	@Autowired
	public GameOfThreeManager(
			@Value("${domain.defaults.maxRandomValue:99999999}") int maxRandomValue,
			@Value("${domain.defaults.defaultGameMode:COMPUTER_VS_COMPUTER}") GameMode defaultGameMode,
			@Value("${player.name}") String currentPlayerName,
			KafkaPublisher kafkaPublisher,
			HumanPlayer humanPlayer,
			ComputerPlayer computerPlayer,
			UserInteraction userInteraction) {
		this(maxRandomValue,
				defaultGameMode,
				currentPlayerName,
				kafkaPublisher,
				humanPlayer,
				computerPlayer,
				userInteraction,
				new Random());
	}

	GameOfThreeManager(int maxRandomValue,
					   GameMode defaultGameMode,
					   String currentPlayerName,
					   KafkaPublisher kafkaPublisher,
					   HumanPlayer humanPlayer,
					   ComputerPlayer computerPlayer,
					   UserInteraction userInteraction,
					   Random random) {
		this.maxRandomValue = Optional.of(maxRandomValue)
				.filter(x -> x >= 2)
				.orElseThrow(() -> new InvalidConfigurationException("Configured max value cannot be less than 2"));
		this.defaultGameMode = defaultGameMode;
		this.currentPlayerName = currentPlayerName;
		this.kafkaPublisher = kafkaPublisher;
		this.userInteraction = userInteraction;
		this.random = random;

		EnumMap<Role, Player> players = new EnumMap<>(Role.class);
		players.put(Role.PLAYER, humanPlayer);
		players.put(Role.COMPUTER, computerPlayer);
		this.playersAccordingToRoles = players;

	}
	// @formatter:on

	/**
	 * Starts a new game with a random value.
	 * @param gameMode The game mode type. If null, the game will use a default game mode
	 * type.
	 */
	public void newGame(GameMode gameMode) {
		newGame(random.nextInt(maxRandomValue - 2) + 2, gameMode);
	}

	/**
	 * Starts a new game with the provided value.
	 * @param value The value of the game mode. Cannot be less than
	 * {@link GameOfThree#MOD}
	 * @param gameMode The game mode type. If null, the game will use a default game mode
	 * type.
	 */
	public void newGame(int value, GameMode gameMode) {
		gameMode = Optional.ofNullable(gameMode).orElse(defaultGameMode);
		if (value < GameOfThree.MOD) {
			throw new InvalidInitialGameValueException(value);
		}

		var newGame = new GameOfThree(value, gameMode, null);
		var gameData = newGame.initialGameData();
		userInteraction.tellThatTheGameHasBegun(currentPlayerName, gameData.getResultingNumber());
		kafkaPublisher.publish(convertGameData(gameData));
	}

	/**
	 * Will proceed the game based on the game data. It means it will call the next player
	 * informing him / her what is the current state of the game, asking for him / her to
	 * play and then proceeding with the game. If the game is finished, inform the player
	 * too.
	 * @param gameData the available game data for playing. Should not be null.
	 */
	public void proceedGame(GameData gameData) {
		gameData = Optional.ofNullable(gameData)
				.orElseThrow(() -> new GameStateException("Tried to provide null game data"));
		var game = new GameOfThree(gameData.getResultingNumber(), gameData.getGameMode(),
				gameData.getGameMode().switchRole(gameData.getCurrentRole()));

		userInteraction.tellUserValueHasJustArrived(gameData.getResultingNumber(), currentPlayerName);

		var player = playersAccordingToRoles.get(game.getCurrentRole());
		var newGameData = player.play(game);

		userInteraction.tellThatPlayerHasPlayedAndWhatIsTheCurrentData(currentPlayerName, gameData.getResultingNumber(),
				newGameData.getAdded(), newGameData.getResultingNumber());

		if (newGameData.isFinished()) {
			userInteraction.tellWhichUserWon(currentPlayerName);
		}
		else {
			kafkaPublisher.publish(convertGameData(newGameData));
		}

	}

	private PortableGameData convertGameData(GameData gameData) {
		// @formatter:off
		return new PortableGameData(gameData.getAdded(),
				gameData.getResultingNumber(),
				gameData.getGameMode().name(),
				Optional.ofNullable(gameData.getCurrentRole()).map(Role::name).orElse(null));
		// @formatter:on
	}

}
