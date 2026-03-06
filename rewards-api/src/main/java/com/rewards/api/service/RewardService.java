package com.rewards.api.service;

import java.time.LocalDate;

import com.rewards.api.dto.RewardResponse;
import com.rewards.api.exception.RewardException;

public interface RewardService {

    RewardResponse calculateRewards(int customerId,
                                    Integer months,
                                    LocalDate startDate,
                                    LocalDate endDate)
            throws RewardException;
}