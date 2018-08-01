package com.invoices.repository;

import com.invoices.domain.CurrencyRates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author psoutzis
 * The repository for the CurrencyRates Entity
 */
@Repository
public interface CurrencyRatesRepo extends JpaRepository<CurrencyRates, Long> {
    /*Method to get column names
    @Query("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'currency_rates'")
    CurrencyRates getCurrencyRatesByColumnName();*/
}
