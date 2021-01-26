package com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidInitialGameValueException extends RuntimeException {

	private final int initialValue;

}
