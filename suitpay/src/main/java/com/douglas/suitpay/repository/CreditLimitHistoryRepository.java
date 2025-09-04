package com.douglas.suitpay.repository;

import com.douglas.suitpay.domain.CreditLimitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditLimitHistoryRepository extends JpaRepository<CreditLimitHistory, Long> {
    Page<CreditLimitHistory> findByCustomerId(Long customerId, Pageable pageable);
}
