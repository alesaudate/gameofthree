package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameOfThreeManager;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.request.StartGameParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/api/game")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GameOfThreeController {

	GameOfThreeManager gameOfThreeManager;

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void newGame(@RequestBody(required = false) StartGameParams startGameParams) {
		Optional.ofNullable(startGameParams).ifPresentOrElse(this::startGameWithParams,
				() -> gameOfThreeManager.newGame(null));
	}

	private void startGameWithParams(StartGameParams startGameParams) {
		Optional.ofNullable(startGameParams.getInitialValue()).ifPresentOrElse(
				value -> gameOfThreeManager.newGame(value, startGameParams.getGameMode()),
				() -> gameOfThreeManager.newGame(startGameParams.getGameMode()));
	}

}
