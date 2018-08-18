package com.invoices.repository;

import com.invoices.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Portfolio Entity
 * @author psoutzis
 */
@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    Portfolio getPortfolioById(Long id);
}
