package com.invoices.service;

import com.invoices.domain.BankAccount;
import com.invoices.repository.BankAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated as a service.
 * It is the service bean for the BankAccounts entity
 * @author psoutzis
 */
@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepo bankAccountRepo;

    /**
     * @param id The primary key of the record to return
     * @return The BankAccount object whose id is equal to the method argument
     */
    public BankAccount getRecord(Long id){

        return bankAccountRepo.getBankAccountById(id);
    }

    /**
     * @return A collection of all the BankAccount objects in the database
     */
    public List<BankAccount> getBankAccounts(){

        return bankAccountRepo.findAll();
    }
}
