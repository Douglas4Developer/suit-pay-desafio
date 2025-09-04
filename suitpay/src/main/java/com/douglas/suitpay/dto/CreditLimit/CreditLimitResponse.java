package com.douglas.suitpay.dto.CreditLimit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreditLimitResponse {
    private BigDecimal limit;
}