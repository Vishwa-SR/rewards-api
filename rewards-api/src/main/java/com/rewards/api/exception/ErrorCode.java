package com.rewards.api.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

	CUSTOMER_NOT_FOUND("Invalid customer Id", HttpStatus.NOT_FOUND),
	INVALID_MONTH("Enter valid month value 1 to 12", HttpStatus.BAD_REQUEST),
	INVALID_PARAMS("Cannot provide both months and date range", HttpStatus.BAD_REQUEST),
	INVALID_DATE("Start date must be before end date", HttpStatus.BAD_REQUEST),
	NO_TRANSACTION_FOUND("No transactions found", HttpStatus.NOT_FOUND);

	private final String message;
	private final HttpStatus status;

	ErrorCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public HttpStatus getStatus() {
		return status;
	}
}