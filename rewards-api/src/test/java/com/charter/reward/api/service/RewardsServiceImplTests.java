package com.charter.reward.api.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.charter.reward.api.dto.RewardResponse;
import com.charter.reward.api.entity.Customer;
import com.charter.reward.api.entity.Transactions;
import com.charter.reward.api.exception.BadRequestException;
import com.charter.reward.api.exception.NotFoundException;
import com.charter.reward.api.repository.CustomerRepository;
import com.charter.reward.api.repository.TransactionRepository;
import com.charter.reward.api.service.RewardServiceImpl;

@ExtendWith(MockitoExtension.class)
class RewardsServiceImplTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Test
    void calculateRewards_whenCustomerNotFound_thenThrowException() {

        when(customerRepository.findById(99)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> rewardService.calculateRewards(99, null, null, null));

        assertEquals("Customer not found for customerId: 99", ex.getMessage());
    }

    @Test
    void calculateRewards_whenInvalidMonth_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, 13, null, null));

        assertEquals("Invalid months value: 13. It should be between 1 and 12", ex.getMessage());
    }

    @Test
    void calculateRewards_whenInvalidDateRange_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, null,
                        LocalDate.of(2026, 3, 3),
                        LocalDate.of(2026, 2, 2)));

        assertEquals(
                "Invalid date range: startDate (2026-03-03) must be before endDate (2026-02-02)",
                ex.getMessage());
    }

    @Test
    void calculateRewards_whenOnlyStartDateProvided_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, null,
                        LocalDate.of(2026, 1, 1), null));

        assertEquals("Invalid request: Both startDate and endDate must be provided together",
                ex.getMessage());
    }

    @Test
    void calculateRewards_whenOnlyEndDateProvided_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, null,
                        null, LocalDate.of(2026, 1, 1)));

        assertEquals("Invalid request: Both startDate and endDate must be provided together",
                ex.getMessage());
    }

    @Test
    void calculateRewards_whenMonthsAndStartDateProvided_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, 3,
                        LocalDate.of(2026, 1, 1), null));

        assertEquals("Invalid request: Provide either 'months' OR 'startDate & endDate', not both",
                ex.getMessage());
    }

    @Test
    void calculateRewards_whenMonthsAndEndDateProvided_thenThrowException() {

        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> rewardService.calculateRewards(1, 3,
                        null, LocalDate.of(2026, 1, 1)));

        assertEquals("Invalid request: Provide either 'months' OR 'startDate & endDate', not both",
                ex.getMessage());
    }

    @Test
    void calculateRewards_whenNoTransactionsFound_thenThrowException() {

        Customer customer = new Customer();
        customer.setCustomerId(1);

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(3);

        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end))
                .thenReturn(Arrays.asList());

        NotFoundException ex = assertThrows(NotFoundException.class,
                () -> rewardService.calculateRewards(1, null, null, null));

        assertEquals("No transactions found for customerId: 1 in the given date range",
                ex.getMessage());

        verify(transactionRepository)
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end);
    }

    @Test
    void calculateRewards_whenValidRequest_thenReturnRewardResponse() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("Vishwa");

        Transactions t1 = new Transactions();
        t1.setAmount(120);
        t1.setTransactionDate(LocalDate.now());

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        LocalDate end = LocalDate.now();
        LocalDate start = end.minusMonths(3);

        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end))
                .thenReturn(Arrays.asList(t1));

        RewardResponse response = rewardService.calculateRewards(1, null, null, null);

        assertEquals(1, response.getCustomerId());
        assertEquals("Vishwa", response.getCustomerName());
        assertEquals(90.0, response.getTotalRewards());

        verify(transactionRepository)
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end);
    }

    @Test
    void calculateRewards_WhenMultipleMonths_thenCorrectGrouping() {

        Customer customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("Vishwa");

        Transactions janTxn = new Transactions();
        janTxn.setAmount(120);
        janTxn.setTransactionDate(LocalDate.of(2026, 1, 10));

        Transactions febTxn = new Transactions();
        febTxn.setAmount(150);
        febTxn.setTransactionDate(LocalDate.of(2026, 2, 10));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));

        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 1);

        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end))
                .thenReturn(Arrays.asList(janTxn, febTxn));

        RewardResponse response = rewardService.calculateRewards(1, null, start, end);
        assertEquals(90.0, response.getMonthlyRewards().get("Jan-2026"));
        assertEquals(2, response.getMonthlyRewards().size());
        assertEquals(240.0, response.getTotalRewards());
        verify(transactionRepository)
                .findByCustomerCustomerIdAndTransactionDateBetween(1, start, end);
    }
}