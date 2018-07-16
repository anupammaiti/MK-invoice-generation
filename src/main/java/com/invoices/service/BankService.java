package com.invoices.service;

import com.invoices.domain.Bank;
import com.invoices.repository.BankRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    @Autowired
    private BankRepo bankRepo;

    public List<Bank> getAvailableBanks(){
        return bankRepo.findAll();
    }
}
