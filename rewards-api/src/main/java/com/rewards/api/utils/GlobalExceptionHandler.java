package com.rewards.api.utils;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rewards.api.exception.ErrorCode;
import com.rewards.api.exception.RewardException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	public static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(RewardException.class)
	public ResponseEntity<ErrorResponse> handleRewardException(RewardException ex) {

		ErrorCode code = ex.getErrorCode();

		ErrorResponse error = new ErrorResponse(code.getMessage(), code.getStatus().value(), LocalDateTime.now());

		LOGGER.error(code.getMessage(), ex);

		return new ResponseEntity<>(error, code.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {

		ErrorResponse error = new ErrorResponse(
				"An unexpected error occurred",
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				LocalDateTime.now());

		LOGGER.error("Unexpected error: {}", ex.getMessage(), ex);

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}