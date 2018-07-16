package com.invoices.repository;

import com.invoices.domain.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyRateRepo extends JpaRepository<CurrencyRate, Long> {

    CurrencyRate getCurrencyRateByCurrencyRateId(Long id);
}
