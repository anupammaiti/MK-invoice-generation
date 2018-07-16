package com.invoices.repository;

import com.invoices.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {
    BankAccount getBankAccountById(Long id);
}
