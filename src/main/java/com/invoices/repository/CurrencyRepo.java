package com.invoices.repository;

import com.invoices.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Long> {

    Currency getCurrencyByCurrencyCode(String code);
}
