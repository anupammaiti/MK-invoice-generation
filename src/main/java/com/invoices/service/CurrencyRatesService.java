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

    /**
     * If CurrencyRates id is null, a new record will be saved to database.
     * @param from The currency to convert from
     * @param to The currency to convert to
     * @param currencyRates The CurrencyRates object to use its mutators and finally return.
     * @param rate The exchange rate to save to CurrencyRates object. If this is null, exchange rate is automatically
     *             generated, using getExchangeRateFor(from, to) method.
     * @return The initialized CurrencyRates record, that is now stored in the database.
     */
    public CurrencyRates generateExchangeRate(Currency from, Currency to,CurrencyRates currencyRates, Float rate){
        setBaseAndTarget(from, to, currencyRates);
        if(rate!=null)
            currencyRates.setExchangeRate(rate);
        else
            setCurrentRate(currencyRates);
        save(currencyRates);

        return currencyRates;
    }

    /**
     * Method will take a parameter CurrencyRates and add a Currency From and a Currency To
     * @param from the Currency object to save to the CurrencyRates object. (Currency to convert from)
     * @param to the Currency object to save to the CurrencyRates object. (Currency to convert to)
     * @param currencyRates the CurrencyRates object to modify
     */
    private void setBaseAndTarget(Currency from, Currency to, CurrencyRates currencyRates){
        currencyRates.setFromCurrency(from);
        currencyRates.setToCurrency(to);
    }

    /**
     * This method will be called whenever an invoice is issued, so the
     * current exchange rate is added to the passed CurrencyRates object.
     * @param currencyRates the object to store at, the fx rate that will be fetched.
     * @return the record holding current exchange rate
     */
    private void setCurrentRate(CurrencyRates currencyRates){
        String baseCurrency = currencyRates.getFromCurrency().getCurrencyCode();
        String targetCurrency = currencyRates.getToCurrency().getCurrencyCode();
        Float exchangeRate = getExchangeRateFor(baseCurrency,targetCurrency);
        currencyRates.setExchangeRate(exchangeRate);
    }

    /**
     * Method will delegate requests to a chain of Exchange providers, until a
     * satisfactory result is received
     * @param baseCurrency The currency to convert from
     * @param targetCurrency The currency to convert to
     * @return a float value of the current exchange rate of given base and target
     */
    private Float getExchangeRateFor(String baseCurrency, String targetCurrency){
        ExchangeRate rate = ExchangeRateProviderHandler.provider.getExchangeRate(baseCurrency, targetCurrency);
        NumberValue factor = rate.getFactor();

        return factor.floatValue();
    }

    /**
     * Method will insert a CurrencyRates object to the database
     * @param currencyRates The CurrencyRates object to save
     */
    private void save(CurrencyRates currencyRates){

        currencyRatesRepo.save(currencyRates);
    }

}
