package com.rewards.api.utils;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rewards.api.exception.ErrorCode;
import com.rewards.api.exception.RewardException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

	public static final Logger LOGGER = LogManager.getLogger(ExceptionControllerAdvice.class);

	@ExceptionHandler(RewardException.class)
	public ResponseEntity<ErrorInfo> rewardExceptionHandler(RewardException ex) {

		ErrorCode code = ex.getErrorCode();

		ErrorInfo error = new ErrorInfo(code.getMessage(), code.getStatus().value(), LocalDateTime.now());

		LOGGER.error(code.getMessage(), ex);

		return new ResponseEntity<>(error, code.getStatus());
	}
}