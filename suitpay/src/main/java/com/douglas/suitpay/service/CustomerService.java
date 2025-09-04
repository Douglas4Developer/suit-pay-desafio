package com.douglas.suitpay.service;


import java.math.BigDecimal;

import com.douglas.suitpay.dto.Customer.CreateCustomerRequest;
import com.douglas.suitpay.dto.Customer.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitResponse;
import com.douglas.suitpay.dto.CreditLimit.CreditLimitHistoryResponse;

public interface CustomerService {
    //(crédito)
    CreditLimitResponse getCreditLimit(Long customerId);
    CreditLimitResponse updateCreditLimit(Long customerId, BigDecimal newLimit, String changedByUsername);
    Page<CreditLimitHistoryResponse> getCreditLimitHistory(Long customerId, Pageable pageable);

    //CRUD
    CustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerResponse getCustomer(Long id);
    Page<CustomerResponse> listCustomers(Pageable pageable);
}
