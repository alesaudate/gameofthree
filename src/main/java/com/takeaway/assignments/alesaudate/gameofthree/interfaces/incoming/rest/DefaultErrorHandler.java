package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest;

import com.takeaway.assignments.alesaudate.gameofthree.domain.exceptions.InvalidInitialGameValueException;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.error.ErrorMessage;
import com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.error.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class DefaultErrorHandler {

	@ExceptionHandler(InvalidInitialGameValueException.class)
	public ResponseEntity<ErrorResponse> handleInvalidValueException(InvalidInitialGameValueException exception) {
		return ResponseEntity.badRequest().body(buildErrorResponse(
				String.format("The supplied value to start the game is invalid: %d", exception.getInitialValue())));
	}

	private ErrorResponse buildErrorResponse(String message) {
		return new ErrorResponse(List.of(new ErrorMessage(message)));
	}

}
