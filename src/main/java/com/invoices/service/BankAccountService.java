package com.invoices.service;

import com.invoices.domain.BankAccount;
import com.invoices.repository.BankAccountRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepo bankAccountRepo;

    @Getter@Setter
    private BankAccount bankAccount;

    public BankAccount getRecord(BankAccount bankAccount){
        return bankAccountRepo.getBankAccountById(bankAccount.getId());
    }

    public List<BankAccount> getBankAccounts(){

        return bankAccountRepo.findAll();
    }
}
