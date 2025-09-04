package com.douglas.suitpay.service;

import com.douglas.suitpay.domain.CreditLimitHistory;
import com.douglas.suitpay.domain.Customer;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitHistoryResponse;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitResponse;
import com.douglas.suitpay.dto.Customer.CreateCustomerRequest;
import com.douglas.suitpay.dto.Customer.CustomerResponse;
import com.douglas.suitpay.exception.BusinessException;
import com.douglas.suitpay.repository.CreditLimitHistoryRepository;
import com.douglas.suitpay.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepository customerRepository;
    private CreditLimitHistoryRepository historyRepository;

    @Value("${credit.vip.min:1000.00}")
    private BigDecimal vipMinLimit;

    @Transactional
    @Retryable(
            value = OptimisticLockingFailureException.class,
            maxAttempts = 3
    )
    @PreAuthorize("hasRole('CREDIT_LIMIT_ADMIN')")
    public CreditLimitResponse updateCreditLimit(Long customerId, BigDecimal newLimit, String username) {
        if (newLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Limite não pode ser negativo.");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));

        if (customer.isVip() && newLimit.compareTo(vipMinLimit) < 0) {
            throw new BusinessException("Clientes VIP precisam ter limite ≥ " + vipMinLimit);
        }

        BigDecimal oldLimit = customer.getCreditLimit();
        customer.setCreditLimit(newLimit);

        customerRepository.save(customer);

        historyRepository.save(
                CreditLimitHistory.builder()
                        .customerId(customerId)
                        .oldLimit(oldLimit)
                        .newLimit(newLimit)
                        .changedBy(username)
                        .changedAt(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );

        return new CreditLimitResponse(newLimit);
    }

    public CreditLimitResponse getCreditLimit(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
        return new CreditLimitResponse(customer.getCreditLimit());
    }

    public Page<CreditLimitHistoryResponse> getCreditLimitHistory(Long customerId, Pageable pageable) {
        return historyRepository.findByCustomerId(customerId, pageable)
                .map(h -> new CreditLimitHistoryResponse(h.getOldLimit(), h.getNewLimit(), h.getChangedBy(), h.getChangedAt()));
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest req) {
        if (req.getCreditLimit().compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Limite não pode ser negativo.");
        }
        if (Boolean.TRUE.equals(req.getIsVip()) &&
                req.getCreditLimit().compareTo(vipMinLimit) < 0) {
            throw new BusinessException("Clientes VIP devem possuir limite mínimo de " + vipMinLimit + ".");
        }

        Customer c = Customer.builder()
                .isVip(req.getIsVip())
                .creditLimit(req.getCreditLimit())
                .build();

        customerRepository.save(c);

        return CustomerResponse.builder()
                .id(c.getId())
                .isVip(c.isVip())
                .creditLimit(c.getCreditLimit())
                .version(c.getVersion())
                .build();
    }

    public CustomerResponse getCustomer(Long id) {
        Customer c = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado."));
        return CustomerResponse.builder()
                .id(c.getId())
                .isVip(c.isVip())
                .creditLimit(c.getCreditLimit())
                .version(c.getVersion())
                .build();
    }

    public Page<CustomerResponse> listCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(c -> CustomerResponse.builder()
                        .id(c.getId())
                        .isVip(c.isVip())
                        .creditLimit(c.getCreditLimit())
                        .version(c.getVersion())
                        .build());
    }
}