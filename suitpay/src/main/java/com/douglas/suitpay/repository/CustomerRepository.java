package com.douglas.suitpay.repository;

import com.douglas.suitpay.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
