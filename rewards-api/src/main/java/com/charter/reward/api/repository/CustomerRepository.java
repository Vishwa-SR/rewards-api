package com.charter.reward.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.charter.reward.api.entity.Customer;

@Repository

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}