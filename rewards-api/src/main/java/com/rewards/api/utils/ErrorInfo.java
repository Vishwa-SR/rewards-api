package com.rewards.api.utils;

import java.time.LocalDateTime;

public class ErrorInfo {

	private String errorMessage;
	private Integer errorCode;
	private LocalDateTime timestamp;

	public ErrorInfo() {
	}

	public ErrorInfo(String errorMessage, Integer errorCode, LocalDateTime timestamp) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
		this.timestamp = timestamp;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}