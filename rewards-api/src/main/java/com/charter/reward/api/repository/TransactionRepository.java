package com.charter.reward.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.charter.reward.api.entity.Transactions;

@Repository

public interface TransactionRepository extends JpaRepository<Transactions, Integer> {

	List<Transactions> findByCustomerCustomerIdAndTransactionDateBetween(int customerId, LocalDate startDate,
			LocalDate endDate);
}