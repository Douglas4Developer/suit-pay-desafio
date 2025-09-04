package com.douglas.suitpay.dto.CreditLimit;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditLimitRequest {
    @NotNull
    @DecimalMin("0.00")
    private BigDecimal newLimit;
}



