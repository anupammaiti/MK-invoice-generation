package com.invoices.repository;

import com.invoices.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author psoutzis
 * The repository for the Portfolio Entity
 */
@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {
    Portfolio getPortfolioByPortfolioCode(String code);
    Portfolio getPortfolioById(Long id);
}
