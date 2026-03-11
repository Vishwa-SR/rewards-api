package com.rewards.api.dto;

import java.util.Map;

public class RewardResponse {

	private int customerId;
	private String customername;
	private Map<String, Integer> monthlyRewards;
	private int totalRewards;

	public RewardResponse() {
	}

	public RewardResponse(int customerId, String customername, Map<String, Integer> monthlyRewards, int totalRewards) {

		super();
		this.customerId = customerId;
		this.customername = customername;
		this.monthlyRewards = monthlyRewards;
		this.totalRewards = totalRewards;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public Map<String, Integer> getMonthlyRewards() {
		return monthlyRewards;
	}

	public void setMonthlyRewards(Map<String, Integer> monthlyRewards) {
		this.monthlyRewards = monthlyRewards;
	}

	public int getTotalRewards() {
		return totalRewards;
	}

	public void setTotalRewards(int totalRewards) {
		this.totalRewards = totalRewards;
	}
}