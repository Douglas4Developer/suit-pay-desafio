package com.douglas.suitpay.dto.Customer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCustomerRequest {
    @NotNull
    private Boolean isVip;

    @NotNull
    @DecimalMin(value = "0.00")
    private BigDecimal creditLimit;
}