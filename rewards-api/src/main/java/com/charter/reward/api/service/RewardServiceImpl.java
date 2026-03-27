package com.charter.reward.api.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.charter.reward.api.dto.RewardResponse;
import com.charter.reward.api.entity.Customer;
import com.charter.reward.api.entity.Transactions;
import com.charter.reward.api.exception.NotFoundException;
import com.charter.reward.api.repository.CustomerRepository;
import com.charter.reward.api.repository.TransactionRepository;
import com.charter.reward.api.util.ValidationUtils;

@Service
public class RewardServiceImpl implements RewardService {

	private static final Logger logger = LoggerFactory.getLogger(RewardServiceImpl.class);

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public RewardResponse calculateRewards(int customerId, Integer months, LocalDate startDate, LocalDate endDate) {

		logger.info("Starting reward calculation for customerId: {}", customerId);

		Customer customer = customerRepository.findById(customerId).orElseThrow(() -> {
			logger.error("Customer not found for customerId: {}", customerId);
			return new NotFoundException("Customer not found for customerId: " + customerId);
		});

		LocalDate[] range = determineDateRange(months, startDate, endDate);

		logger.info("Fetching transactions between {} and {}", range[0], range[1]);

		List<Transactions> transactions = transactionRepository
				.findByCustomerCustomerIdAndTransactionDateBetween(customerId, range[0], range[1]);

		if (transactions.isEmpty()) {
			logger.error("No transactions found for customerId: {} in given date range", customerId);
			throw new NotFoundException(
					"No transactions found for customerId: " + customerId + " in the given date range");
		}

		Map<String, Double> monthlyRewards = new LinkedHashMap<>();
		double totalPoints = 0;

		for (Transactions t : transactions) {

			double points = calculatePoints(t.getAmount());

			String month = t.getTransactionDate().format(DateTimeFormatter.ofPattern("MMM-yyyy"));

			monthlyRewards.put(month, monthlyRewards.getOrDefault(month, 0.0) + points);

			totalPoints += points;
		}

		logger.info("Reward calculation completed for customerId: {} with total points: {}", customerId, totalPoints);

		return new RewardResponse(customerId, customer.getCustomerName(), monthlyRewards, totalPoints);
	}

	private LocalDate[] determineDateRange(Integer months, LocalDate startDate, LocalDate endDate) {

		ValidationUtils.validateInput(months, startDate, endDate);
		
		if (months != null) {
			LocalDate end = LocalDate.now();
			LocalDate start = end.minusMonths(months);
			return new LocalDate[] { start, end };
		}

		if (startDate != null && endDate != null) {
			return new LocalDate[] { startDate, endDate };
		}

		LocalDate end = LocalDate.now();
		LocalDate start = end.minusMonths(3);

		return new LocalDate[] { start, end };
	}

	private double calculatePoints(double amount) {

		if (amount <= 50)
			return 0;

		if (amount <= 100)
			return amount - 50;

		return (amount - 100) * 2 + 50;
	}
}