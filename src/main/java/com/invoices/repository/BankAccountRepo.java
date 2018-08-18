package com.invoices.repository;

import com.invoices.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository for the BankAccount Entity
 * @author psoutzis
 */
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    BankAccount getBankAccountById(Long id);
}
