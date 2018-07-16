package com.invoices.service;

import com.invoices.domain.Currency;
import com.invoices.repository.CurrencyRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the Currency entity
 */
@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepo currencyRepo;

    @Getter @Setter
    private Currency currency;

    public Currency getRecord(Long id){
        return currencyRepo.getCurrencyByCurrencyId(id);
    }

    public List<Currency> getAvailableCurrencies(){

        return currencyRepo.findAll();
    }
}
