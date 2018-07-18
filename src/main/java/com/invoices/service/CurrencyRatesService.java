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

    public void deleteRecord(Long id){
        currencyRatesRepo.deleteById(id);
    }

    /**
     * This method will be called whenever an invoice is issued, so the
     * current exchange rate is fetched and stored to the database, before returning
     * the currencyRates entity back to the invoice.
     * @return the record holding current exchange rate
     */
    public CurrencyRates getCurrentRateAndSave(CurrencyRates currencyRates){
        String baseCurrency = currencyRates.getFromCurrency().getCurrencyCode();
        String targetCurrency = currencyRates.getToCurrency().getCurrencyCode();
        currencyRates.setExchangeRate(getExchangeRateFor(baseCurrency,targetCurrency));
        save(currencyRates);

        return currencyRates;
    }

    /**
     * Method will delegate requests to a chain of Exchange providers, until a
     * satisfactory result is received
     * @param baseCurrency The currency to convert from
     * @param targetCurrency The currency to convert to
     * @return a float value of the current exchange rate of given base and target
     */
    private Float getExchangeRateFor(String baseCurrency, String targetCurrency){
        ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate rate = provider.getExchangeRate(baseCurrency, targetCurrency);
        NumberValue factor = rate.getFactor();

        return factor.floatValue();
    }

    private void save(CurrencyRates currencyRates){

        currencyRatesRepo.save(currencyRates);
    }

}
