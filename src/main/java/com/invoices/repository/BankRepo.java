package com.invoices.repository;

import com.invoices.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * The repository for the Bank Entity
 * @author psoutzis
 */
public interface BankRepo extends JpaRepository<Bank, Long> {
}
