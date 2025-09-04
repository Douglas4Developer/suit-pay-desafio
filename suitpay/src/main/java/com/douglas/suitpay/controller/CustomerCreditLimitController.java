package com.douglas.suitpay.controller;

import com.douglas.suitpay.dto.CreditLimit.CreditLimitHistoryResponse;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitRequest;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitResponse;
import com.douglas.suitpay.service.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers/{customerId}/credit-limit")
@RequiredArgsConstructor
public class CustomerCreditLimitController {

    private final CustomerServiceImpl service;

    @GetMapping
    public ResponseEntity<CreditLimitResponse> getCreditLimit(@PathVariable("customerId") Long customerId) {
        return ResponseEntity.ok(service.getCreditLimit(customerId));
    }

    @PutMapping
    public ResponseEntity<CreditLimitResponse> updateCreditLimit(
            @PathVariable("customerId") Long customerId,
            @Valid @RequestBody CreditLimitRequest request,
            Authentication auth) {
        return ResponseEntity.ok(service.updateCreditLimit(customerId, request.getNewLimit(), auth.getName()));
    }

    @GetMapping("/history")
    public ResponseEntity<Page<CreditLimitHistoryResponse>> getHistory(
            @PathVariable("customerId") Long customerId,
            Pageable pageable) {
        return ResponseEntity.ok(service.getCreditLimitHistory(customerId, pageable));
    }
}
