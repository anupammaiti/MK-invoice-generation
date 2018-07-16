package com.invoices.service;

import com.invoices.domain.Currency;
import com.invoices.repository.CurrencyRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepo currencyRepo;

    @Getter @Setter
    private Currency currency;

    public Currency getRecord(Currency currency){
        return currencyRepo.getCurrencyByCurrencyCode(currency.getCurrencyCode());
    }

    public List<Currency> getAvailableCurrencies(){

        return currencyRepo.findAll();
    }
}
