package com.rewards.api.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewards.api.dto.RewardResponse;
import com.rewards.api.entity.Customer;
import com.rewards.api.entity.Transactions;
import com.rewards.api.exception.ErrorCode;
import com.rewards.api.exception.RewardException;
import com.rewards.api.repository.CustomerRepository;
import com.rewards.api.repository.TransactionRepository;

@Service
public class RewardServiceImpl {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CustomerRepository customerRepository;

	public RewardResponse calculateRewards(int customerId, Integer months, LocalDate startDate, LocalDate endDate)
			throws RewardException {

		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new RewardException(ErrorCode.CUSTOMER_NOT_FOUND));

		validateInput(months, startDate, endDate);

		LocalDate[] range = determineDateRange(months, startDate, endDate);

		List<Transactions> transactions = transactionRepository
				.findByCustomerCustomerIdAndTransactionDateBetween(customerId, range[0], range[1]);

		if (transactions.isEmpty()) {
			throw new RewardException(ErrorCode.NO_TRANSACTION_FOUND);
		}

		Map<String, Integer> monthlyRewards = new HashMap<>();
		int totalPoints = 0;

		for (Transactions t : transactions) {

			int points = calculatePoints(t.getAmount());

			String month = t.getTransactionDate().getMonth().toString();

			monthlyRewards.put(month, monthlyRewards.getOrDefault(month, 0) + points);

			totalPoints += points;
		}

		return new RewardResponse(customerId, customer.getCustomerName(), monthlyRewards, totalPoints);
	}

	private void validateInput(Integer months, LocalDate startDate, LocalDate endDate) throws RewardException {

		if (months != null && startDate != null && endDate != null) {
			throw new RewardException(ErrorCode.INVALID_PARAMS);
		}

		if (months != null && (months < 1 || months > 12)) {
			throw new RewardException(ErrorCode.INVALID_MONTH);
		}

		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new RewardException(ErrorCode.INVALID_DATE);
		}
	}

	private LocalDate[] determineDateRange(Integer months, LocalDate startDate, LocalDate endDate) {

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

	private int calculatePoints(double amount) {

		if (amount <= 50)
			return 0;

		if (amount <= 100)
			return (int) (amount - 50);

		return (int) ((amount - 100) * 2 + 50);
	}
}