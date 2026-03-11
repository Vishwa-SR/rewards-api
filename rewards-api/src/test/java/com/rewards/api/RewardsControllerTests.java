package com.rewards.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.rewards.api.controller.RewardController;
import com.rewards.api.dto.RewardResponse;
import com.rewards.api.exception.ErrorCode;
import com.rewards.api.exception.RewardException;
import com.rewards.api.service.RewardServiceImpl;

@WebMvcTest(RewardController.class)
class RewardsControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@SuppressWarnings("removal")
	@MockBean
	private RewardServiceImpl rewardService;

	@Test
	void customernotFoundTest() throws Exception {

		Mockito.when(rewardService.calculateRewards(99, null, null, null))
				.thenThrow(new RewardException(ErrorCode.CUSTOMER_NOT_FOUND));

		mockMvc.perform(get("/rewards/{customerId}", 99)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorMessage", Matchers.is("Invalid customer Id")))
				.andExpect(jsonPath("$.errorCode", Matchers.is(404)));
	}

	@Test
	void noTransactionfoundTest() throws Exception {

		Mockito.when(rewardService.calculateRewards(1, null, null, null))
				.thenThrow(new RewardException(ErrorCode.NO_TRANSACTION_FOUND));

		mockMvc.perform(get("/rewards/{customerId}", 1)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errorMessage", Matchers.is("No transactions found")))
				.andExpect(jsonPath("$.errorCode", Matchers.is(404)));
	}

	@Test
	void invalidmonthTest() throws Exception {

		Mockito.when(rewardService.calculateRewards(1, 13, null, null))
				.thenThrow(new RewardException(ErrorCode.INVALID_MONTH));

		mockMvc.perform(get("/rewards/{customerId}", 1).param("months", "13")).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", Matchers.is("Enter valid month value 1 to 12")))
				.andExpect(jsonPath("$.errorCode", Matchers.is(400)));
	}

	@Test
	void invalidStartDateTest() throws Exception {

		Mockito.when(rewardService.calculateRewards(1, null, LocalDate.of(2026, 3, 3), LocalDate.of(2026, 2, 2)))
				.thenThrow(new RewardException(ErrorCode.INVALID_DATE));

		mockMvc.perform(get("/rewards/{customerId}", 1).param("startDate", "2026-03-03").param("endDate", "2026-02-02"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errorMessage", Matchers.is("Start date must be before end date")))
				.andExpect(jsonPath("$.errorCode", Matchers.is(400)));
	}

	@Test
	void successTest() throws Exception {

		RewardResponse response = new RewardResponse();
		response.setCustomerId(1);
		response.setCustomerName("Vishwa");
		response.setTotalRewards(120);

		Mockito.when(rewardService.calculateRewards(1, null, null, null)).thenReturn(response);

		mockMvc.perform(get("/rewards/{customerId}", 1)).andExpect(status().isOk())
				.andExpect(jsonPath("$.customerId").value(1)).andExpect(jsonPath("$.customerName").value("Vishwa"))
				.andExpect(jsonPath("$.totalRewards").value(120));
	}
}