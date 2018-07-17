package com.invoices.service;

import com.invoices.domain.BankAccount;
import com.invoices.repository.BankAccountRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the BankAccounts entity
 */
@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepo bankAccountRepo;

    public BankAccount getRecord(Long id){
        return bankAccountRepo.getBankAccountById(id);
    }

    public List<BankAccount> getBankAccounts(){

        return bankAccountRepo.findAll();
    }
}
