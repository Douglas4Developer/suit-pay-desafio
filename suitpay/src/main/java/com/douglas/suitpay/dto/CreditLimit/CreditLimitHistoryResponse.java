package com.douglas.suitpay.dto.CreditLimit;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CreditLimitHistoryResponse {
    private BigDecimal oldLimit;
    private BigDecimal newLimit;
    private String changedBy;
    private LocalDateTime changedAt;
}
