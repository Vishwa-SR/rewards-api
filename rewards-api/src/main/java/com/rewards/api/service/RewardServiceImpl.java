package com.rewards.api.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rewards.api.dto.RewardResponse;
import com.rewards.api.entity.Customer;
import com.rewards.api.entity.Transactions;
import com.rewards.api.exception.RewardException;
import com.rewards.api.repository.CustomerRepository;
import com.rewards.api.repository.TransactionRepository;

@Service
public class RewardServiceImpl implements RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public RewardResponse calculateRewards(int customerId, Integer months,
                                           LocalDate startDate, LocalDate endDate)
            throws RewardException {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RewardException("Service.CUSTOMER_NOT_FOUND"));

        List<Transactions> transactions = new ArrayList<>();

        if (months != null && startDate == null && endDate == null) {
           if(months>12 || months<1) {
        	throw new RewardException("Service.INVALID_MONTH");
           }

      
            transactions = transactionRepository
                    .findByCustomerCustomerIdAndTransactionDateBetween(
                            customerId,
                            LocalDate.now().minusMonths(months),
                            LocalDate.now());
        }

        else if (months == null && startDate != null && endDate != null) {

            if (startDate.isAfter(endDate)) {
                throw new RewardException("Service.INVALID_DATE");
            }

            transactions = transactionRepository
                    .findByCustomerCustomerIdAndTransactionDateBetween(
                            customerId, startDate, endDate);
        }

        else {
            transactions = transactionRepository
                    .findByCustomerCustomerIdAndTransactionDateBetween(
                            customerId,
                            LocalDate.now().minusMonths(3),
                            LocalDate.now());
        }

        if (transactions.isEmpty()) {
            throw new RewardException("Service.NO_TRANSACTION_FOUND");
        }

        Map<String, Integer> monthlyRewards = new HashMap<>();
        int totalPoints = 0;

        for (Transactions transaction : transactions) {

            int points = calculatePoints(transaction.getAmount());

            String month = transaction.getTransactionDate().getMonth().toString();

            monthlyRewards.put(month,
                    monthlyRewards.getOrDefault(month, 0) + points);

            totalPoints += points;
        }

        return new RewardResponse(
                customerId,
                customer.getCustomerName(),
                monthlyRewards,
                totalPoints
        );
    }

    private int calculatePoints(double amount) {

        int points = 0;

        if (amount > 100) {
            points += (amount - 100) * 2;
            points += 50;
        }

        else if(amount>50) {
            points += (int) (amount - 50);
        }

        return points;
    }
}