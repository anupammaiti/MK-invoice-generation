package com.invoices.service;

import com.invoices.domain.CurrencyRates;
import com.invoices.repository.CurrencyRatesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.NumberValue;
import javax.money.convert.ExchangeRate;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the CurrencyRate entity
 * It uses java's Currency & Money API for fetching exchange rates
 * through an http request.
 */
@Service
public class CurrencyRatesService {
    @Autowired
    private CurrencyRatesRepo currencyRatesRepo;
    //private ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();


    /**
     * Method will delegate requests to a chain of Exchange providers, until a
     * satisfactory result is received
     * @param baseCurrency The currency to convert from
     * @param targetCurrency The currency to convert to
     * @return a float value of the current exchange rate of given base and target
     */
    public Float getExchangeRateFor(String baseCurrency, String targetCurrency){
        ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate rate = provider.getExchangeRate(baseCurrency, targetCurrency);
        NumberValue factor = rate.getFactor();
        Float f = factor.floatValue();

        return f;
    }
    /**
     * This method will be called whenever an invoice is issued, so the
     * current exchange rates are fetched and stored to the database, before returning
     * the currencyRates entity back to the invoice
     * to hold the fetched exchange rates
     * @return the record holding current exchange rates
     */
    public CurrencyRates getCurrentRates(){
        CurrencyRates currencyRates = new CurrencyRates();
        currencyRates.setEuroToGbp(getExchangeRateFor("EUR","GBP"));
        currencyRates.setEuroToUsd(getExchangeRateFor("EUR", "USD"));
        currencyRates.setEuroToJpy(getExchangeRateFor("EUR","JPY"));
        currencyRates.setGbpToUsd(getExchangeRateFor("GBP", "USD"));
        currencyRates.setJpyToUsd(getExchangeRateFor("JPY", "USD"));

        this.save(currencyRates);

        return currencyRates;
    }

    private void save(CurrencyRates currencyRates){
        currencyRatesRepo.save(currencyRates);
    }

}
