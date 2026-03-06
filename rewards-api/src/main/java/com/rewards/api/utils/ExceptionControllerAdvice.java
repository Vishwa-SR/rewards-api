package com.rewards.api.utils;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.rewards.api.exception.RewardException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @Autowired
    Environment environment;

    public static final Logger LOGGER =
            LogManager.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(RewardException.class)
    public ResponseEntity<ErrorInfo> infyBankexceptionHandler(RewardException exception) {

        ErrorInfo error = new ErrorInfo();

        error.setErrorMessage(
                environment.getProperty(exception.getMessage(), exception.getMessage())
        );

        error.setTimestamp(LocalDateTime.now());
        error.setErrorCode(HttpStatus.NOT_FOUND.value());

        LOGGER.error(exception.getMessage(), exception);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}