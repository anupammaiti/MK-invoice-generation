package com.invoices.service;

import com.invoices.domain.Bank;
import com.invoices.repository.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated as a service.
 * It is the service bean for the Bank entity
 * @author psoutzis
 */
@Service
public class BankService {
    @Autowired
    private BankRepo bankRepo;

    /**
     * @return A collection of all the Bank-type objects in the database
     */
    public List<Bank> getAvailableBanks(){

        return bankRepo.findAll();
    }
}
