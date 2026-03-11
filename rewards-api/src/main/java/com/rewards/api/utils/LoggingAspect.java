package com.rewards.api.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

	public static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

	@Before("execution(* com.rewards.api.service.*Impl.*(..))")
	public void logBefore() {
		LOGGER.info("Entering service method");
	}

	@AfterThrowing(pointcut = "execution(* com.rewards.api.service.*Impl.*(..))", throwing = "exception")
	public void logServiceException(Exception exception) {
		LOGGER.error("Exception occurred: {}", exception.getMessage(), exception);
	}
}