package com.invoices.repository;

import com.invoices.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * @author psoutzis
 * The repository for the Bank Entity
 */
public interface BankRepo extends JpaRepository<Bank, Long> {
}
