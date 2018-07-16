package com.invoices.repository;

import com.invoices.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepo extends JpaRepository<Portfolio, Long> {
    Portfolio getPortfolioByPortfolioCode(String code);
}
