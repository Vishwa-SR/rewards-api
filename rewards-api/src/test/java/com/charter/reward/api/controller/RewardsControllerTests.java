package com.charter.reward.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.charter.reward.api.controller.RewardController;
import com.charter.reward.api.dto.RewardResponse;
import com.charter.reward.api.exception.BadRequestException;
import com.charter.reward.api.exception.NotFoundException;
import com.charter.reward.api.service.RewardService;

@WebMvcTest(RewardController.class)
class RewardsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RewardService rewardService;


    @Test
    void calculateRewards_whenCustomerNotFound_thenReturnNotFound() throws Exception {

        Mockito.when(rewardService.calculateRewards(99, null, null, null))
                .thenThrow(new NotFoundException("Customer not found for customerId: 99"));

        mockMvc.perform(get("/rewards/{customerId}", 99))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage", Matchers.is("Customer not found for customerId: 99")))
                .andExpect(jsonPath("$.errorCode", Matchers.is(404)));
    }

    @Test
    void calculateRewards_whenNoTransactionsFound_thenReturnNotFound() throws Exception {

        Mockito.when(rewardService.calculateRewards(1, null, null, null))
                .thenThrow(new NotFoundException("No transactions found for customerId: 1 in the given date range"));

        mockMvc.perform(get("/rewards/{customerId}", 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorMessage",
                        Matchers.is("No transactions found for customerId: 1 in the given date range")))
                .andExpect(jsonPath("$.errorCode", Matchers.is(404)));
    }

 

    @Test
    void calculateRewards_whenInvalidMonth_thenReturnBadRequest() throws Exception {

        Mockito.when(rewardService.calculateRewards(1, 13, null, null))
                .thenThrow(new BadRequestException("Invalid months value: 13. It should be between 1 and 12"));

        mockMvc.perform(get("/rewards/{customerId}", 1)
                .param("months", "13"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode", Matchers.is(400)));
    }

    
  



    @Test
    void calculateRewards_whenDefaultRequest_thenReturnSuccess() throws Exception {

        RewardResponse response = new RewardResponse();
        response.setCustomerId(1);
        response.setCustomerName("Vishwa");
        response.setTotalRewards(120.0);

        Mockito.when(rewardService.calculateRewards(1, null, null, null))
                .thenReturn(response);

        mockMvc.perform(get("/rewards/{customerId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Vishwa"))
                .andExpect(jsonPath("$.totalRewards").value(120.0));
    }

    @Test
    void calculateRewards_whenDateRangeProvided_thenReturnSuccess() throws Exception {

        RewardResponse response = new RewardResponse();
        response.setCustomerId(1);
        response.setCustomerName("Vishwa");
        response.setTotalRewards(300.0);

        Mockito.when(rewardService.calculateRewards(
                1,
                null,
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 3, 1)))
                .thenReturn(response);

        mockMvc.perform(get("/rewards/{customerId}", 1)
                .param("startDate", "2026-01-01")
                .param("endDate", "2026-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Vishwa"))
                .andExpect(jsonPath("$.totalRewards").value(300.0));
    }

    @Test
    void calculateRewards_whenMonthsProvided_thenReturnSuccess() throws Exception {

        RewardResponse response = new RewardResponse();
        response.setCustomerId(1);
        response.setCustomerName("Vishwa");
        response.setTotalRewards(200.0);

        Mockito.when(rewardService.calculateRewards(1, 3, null, null))
                .thenReturn(response);

        mockMvc.perform(get("/rewards/{customerId}", 1)
                .param("months", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Vishwa"))
                .andExpect(jsonPath("$.totalRewards").value(200.0));
               
    }
}
		