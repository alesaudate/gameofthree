package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.request;

import com.takeaway.assignments.alesaudate.gameofthree.domain.GameMode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StartGameParams {

	GameMode gameMode;

	Integer initialValue;

}
