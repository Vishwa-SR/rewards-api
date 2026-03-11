package com.rewards.api.exception;

public class RewardException extends Exception {

	private static final long serialVersionUID = 1L;

	private final ErrorCode errorCode;

	public RewardException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}