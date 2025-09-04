package com.douglas.suitpay.controller;

import com.douglas.suitpay.dto.Customer.CreateCustomerRequest;
import com.douglas.suitpay.dto.Customer.CustomerResponse;
import com.douglas.suitpay.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl service;

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest req) {
        return ResponseEntity.ok(service.createCustomer(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> list(Pageable pageable) {
        return ResponseEntity.ok(service.listCustomers(pageable));
    }
}