package com.invoices.service;

import com.invoices.domain.Currency;
import com.invoices.domain.CurrencyRates;
import com.invoices.repository.CurrencyRatesRepo;
import com.invoices.utils.ExchangeRateProviderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.money.NumberValue;
import javax.money.convert.ExchangeRate;

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


    private CurrencyRates setBaseAndTarget(Currency from, Currency to, CurrencyRates currencyRates){
        currencyRates.setFromCurrency(from);
        currencyRates.setToCurrency(to);

        return currencyRates;
    }

    /**
     * This method will be called whenever an invoice is issued, so the
     * current exchange rate is fetched and stored to the database, before returning
     * the currencyRates entity back to the invoice.
     * @return the record holding current exchange rate
     */
    private CurrencyRates getCurrentRateAndSave(CurrencyRates currencyRates){
        String baseCurrency = currencyRates.getFromCurrency().getCurrencyCode();
        String targetCurrency = currencyRates.getToCurrency().getCurrencyCode();
        Float exchangeRate = getExchangeRateFor(baseCurrency,targetCurrency);
        currencyRates.setExchangeRate(exchangeRate);
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
        //ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();
        ExchangeRate rate = ExchangeRateProviderHandler.provider.getExchangeRate(baseCurrency, targetCurrency);
        NumberValue factor = rate.getFactor();

        return factor.floatValue();
    }

    public CurrencyRates generateExchangeRate(Currency from, Currency to){
        CurrencyRates currencyRates = new CurrencyRates();

        this.setBaseAndTarget(from, to, currencyRates);
        this.getCurrentRateAndSave(currencyRates);

        return currencyRates;
    }

    public void save(CurrencyRates currencyRates){

        currencyRatesRepo.save(currencyRates);
    }

}
