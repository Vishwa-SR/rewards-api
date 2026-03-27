package com.charter.reward.api.dto;

import java.util.Map;

public class RewardResponse {

	private int customerId;
	private String customerName;
	private Map<String, Double> monthlyRewards;
	private double totalRewards;

	public RewardResponse() {
	}

	public RewardResponse(int customerId, String customername, Map<String, Double> monthlyRewards, double totalRewards) {

		super();
		this.customerId = customerId;
		this.customerName = customername;
		this.monthlyRewards = monthlyRewards;
		this.totalRewards = totalRewards;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customername) {
		this.customerName = customername;
	}

	public Map<String, Double> getMonthlyRewards() {
		return monthlyRewards;
	}

	public void setMonthlyRewards(Map<String, Double> monthlyRewards) {
		this.monthlyRewards = monthlyRewards;
	}

	public double getTotalRewards() {
		return totalRewards;
	}

	public void setTotalRewards(double totalRewards) {
		this.totalRewards = totalRewards;
	}
}