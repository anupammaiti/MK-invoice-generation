package com.invoices.service;

import com.invoices.domain.Currency;
import com.invoices.repository.CurrencyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated as a service.
 * It is the service bean for the Currency entity
 * @author psoutzis
 */
@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepo currencyRepo;

    /**
     * @param id The primary key of the record to return
     * @return The Currency object whose id is equal to the method argument
     */
    public Currency getRecord(Long id){

        return currencyRepo.getCurrencyByCurrencyId(id);
    }

    /**
     * @return A collection of all the available currencies in the database
     */
    public List<Currency> getAvailableCurrencies(){

        return currencyRepo.findAll();
    }
}
