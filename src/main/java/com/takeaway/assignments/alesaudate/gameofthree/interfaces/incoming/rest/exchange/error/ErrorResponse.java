package com.takeaway.assignments.alesaudate.gameofthree.interfaces.incoming.rest.exchange.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ErrorResponse {

	private List<ErrorMessage> errorMessages;

}
