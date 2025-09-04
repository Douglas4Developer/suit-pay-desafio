package com.douglas.suitpay.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreditLimitHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    @Column(precision = 18, scale = 2)
    private BigDecimal oldLimit;

    @Column(precision = 18, scale = 2)
    private BigDecimal newLimit;

    private String changedBy;

    private LocalDateTime changedAt;
}
