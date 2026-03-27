package com.charter.reward.api.service;

import java.time.LocalDate;

import com.charter.reward.api.dto.RewardResponse;

public interface RewardService {

	public RewardResponse calculateRewards(int customerId, Integer months, LocalDate startDate, LocalDate endDate);
}