package com.rewards.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import com.rewards.api.dto.RewardResponse;
import com.rewards.api.entity.Customer;
import com.rewards.api.entity.Transactions;
import com.rewards.api.exception.ErrorCode;
import com.rewards.api.exception.RewardException;
import com.rewards.api.repository.CustomerRepository;
import com.rewards.api.repository.TransactionRepository;
import com.rewards.api.service.RewardServiceImpl;

@ExtendWith(MockitoExtension.class)
class RewardsServiceImplTests {
	@Mock
	private TransactionRepository transactionRepository;
	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	private RewardServiceImpl rewardService;

	@Test
	void customernotFoundTest() {
		when(customerRepository.findById(99)).thenReturn(Optional.empty());
		RewardException ex = assertThrows(RewardException.class,
				() -> rewardService.calculateRewards(99, null, null, null));
		assertEquals(ErrorCode.CUSTOMER_NOT_FOUND, ex.getErrorCode());
	}

	@Test
	void invalidmonthTest() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));
		RewardException ex = assertThrows(RewardException.class,
				() -> rewardService.calculateRewards(1, 13, null, null));
		assertEquals(ErrorCode.INVALID_MONTH, ex.getErrorCode());
	}

	@Test
	void invaliddateTest() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));
		RewardException ex = assertThrows(RewardException.class,
				() -> rewardService.calculateRewards(1, null, LocalDate.of(2026, 3, 3), LocalDate.of(2026, 2, 2)));
		assertEquals(ErrorCode.INVALID_DATE, ex.getErrorCode());
	}

	@Test
	void noTransactionFoundTest() {
		when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));
		when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(anyInt(), any(), any()))
				.thenReturn(Arrays.asList());
		RewardException ex = assertThrows(RewardException.class,
				() -> rewardService.calculateRewards(1, null, null, null));
		assertEquals(ErrorCode.NO_TRANSACTION_FOUND, ex.getErrorCode());
	}

	@Test
	void successTest() throws RewardException {
		Customer customer = new Customer();
		customer.setCustomerId(1);
		customer.setCustomerName("Vishwa");
		Transactions t1 = new Transactions();
		t1.setAmount(120);
		t1.setTransactionDate(LocalDate.now());
		when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
		when(transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(anyInt(), any(), any()))
				.thenReturn(Arrays.asList(t1));
		RewardResponse response = rewardService.calculateRewards(1, null, null, null);
		assertEquals(1, response.getCustomerId());
		assertEquals("Vishwa", response.getCustomerName());
		assertEquals(90, response.getTotalRewards());
	}
}