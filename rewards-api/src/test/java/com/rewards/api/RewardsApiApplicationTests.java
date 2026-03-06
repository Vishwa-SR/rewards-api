package com.rewards.api;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.rewards.api.controller.RewardController;
import com.rewards.api.exception.RewardException;
import com.rewards.api.service.RewardService;

@SuppressWarnings("removal")
@WebMvcTest(RewardController.class)
class RewardsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    
	@MockBean
    RewardService rewardService;

    @Test
    void CustomerNotFoundTest() throws Exception {

        Mockito.when(rewardService.calculateRewards(99, null, null, null))
                .thenThrow(new RewardException("Service.CUSTOMER_NOT_FOUND"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/{customerId}", 99))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage",
                        Matchers.is("Invalid customer Id")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode",
                        Matchers.is(404)));
    }

    @Test
    void NoTransactionFound() throws Exception {

        Mockito.when(rewardService.calculateRewards(1, null, null, null))
                .thenThrow(new RewardException("Service.NO_TRANSACTION_FOUND"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/{customerId}", 1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage",
                        Matchers.is("No transaction found for this customer")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode",
                        Matchers.is(404)));
    }

    @Test
    void InvalidMonth() throws Exception {

        Mockito.when(rewardService.calculateRewards(1, 13, null, null))
                .thenThrow(new RewardException("Service.INVALID_MONTH"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/{customerId}", 1)
                        .param("months", "13"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage",
                        Matchers.is("Enter Valid month value 1 to 12")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode",
                        Matchers.is(404)));
    }

    @Test
    void InvalidStartDate() throws Exception {

        Mockito.when(rewardService.calculateRewards(
               1,
                null,
                LocalDate.of(2026, 3, 3),
              LocalDate.of(2026, 2, 2)
        )).thenThrow(new RewardException("Service.INVALID_DATE"));

        mockMvc.perform(MockMvcRequestBuilders.get("/rewards/{customerId}", 1)
                        .param("startDate", "2026-03-03")
                        .param("endDate", "2026-02-02"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage",
                        Matchers.is("Start date should be before EndDate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorCode",
                        Matchers.is(404)));
    }
}