package com.douglas.suitpay.dto.Customer;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private Boolean isVip;
    private BigDecimal creditLimit;
    private Integer version;
}