package com.rewards.api.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rewards.api.dto.RewardResponse;
import com.rewards.api.exception.RewardException;
import com.rewards.api.service.RewardService;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponse> getRewardsForCustomer(

            @PathVariable int customerId,

            @RequestParam(required = false) Integer months,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate

    ) throws RewardException {

        RewardResponse response =
                rewardService.calculateRewards(customerId, months, startDate, endDate);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}