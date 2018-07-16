package com.invoices.service;

import com.invoices.domain.CurrencyRates;
import com.invoices.repository.CurrencyRatesRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the CurrencyRate entity
 *
 * It will use an API for fetching exchange rates through an http request.
 */
@Service
public class CurrencyRatesService {
    @Autowired
    private CurrencyRatesRepo currencyRatesRepo;

    @Getter @Setter
    private CurrencyRates currencyRates;

    /**
     * This method will be called whenever an invoice is issued, so the
     * current exchange rates are fetched and stored to the database
     * @param currencyRates is the CurrencyRates entity object to be initialised
     * to hold the fetched exchange rates
     * @return the record holding current exchange rates
     */
    public CurrencyRates getCurrentRates(CurrencyRates currencyRates){
        //1. Code to get current rates
        //2. Set rates to parsed object
        //3. repository.save(parsed object)

        return currencyRates;
    }

}
