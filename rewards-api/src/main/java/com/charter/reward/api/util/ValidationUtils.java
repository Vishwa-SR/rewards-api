package com.charter.reward.api.util;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.charter.reward.api.exception.BadRequestException;

public class ValidationUtils {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    public static void validateInput(Integer months, LocalDate startDate, LocalDate endDate) {

        if (months != null && (startDate != null || endDate != null)) {
            logger.error("Invalid request: Provide either 'months' OR 'startDate & endDate', not both");
            throw new BadRequestException(
                    "Invalid request: Provide either 'months' OR 'startDate & endDate', not both");
        }

        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            logger.error("Invalid request: Both startDate and endDate must be provided together");
            throw new BadRequestException(
                    "Invalid request: Both startDate and endDate must be provided together");
        }

        if (months != null && (months < 1 || months > 12)) {
            logger.error("Invalid months value: {}", months);
            throw new BadRequestException(
                    "Invalid months value: " + months + ". It should be between 1 and 12");
        }

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            logger.error("Invalid date range: startDate {} endDate {}", startDate, endDate);
            throw new BadRequestException(
                    "Invalid date range: startDate (" + startDate + ") must be before endDate (" + endDate + ")");
        }
    }
}