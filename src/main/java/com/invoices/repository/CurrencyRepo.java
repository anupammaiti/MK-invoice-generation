package com.invoices.repository;

import com.invoices.domain.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Currency Entity
 * @author psoutzis
 */
@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    Currency getCurrencyByCurrencyId(Long id);
}
