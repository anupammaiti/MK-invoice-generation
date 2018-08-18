package com.invoices.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

/**
 * This class is a component and will be auto-detected at
 * system initialization. It is responsible for keeping an updated file for exchange rates
 * It has a static ExchangeRateProvider, which uses the default providers that are offered
 * through Java's Monetary and Currency APIs. Class will update the provider with new data everyday
 * at 8:00, 16:00 and 17:00 with a scheduled cron task.
 * @author psoutzis
 */
@Component
public class ExchangeRateProviderHandler {
    //This is the static object that hold exchange rate data
    public static ExchangeRateProvider provider;

    /**
     * @param currencyCode The currency code of the symbol needed. e.g: 'USD', 'GBP', etc..
     * @return The symbol of the currency code passed as a parameter. Method will return an empty string,
     * in case that the  currency code does not return any matches.<br>
     * e.g:<br><u>USD</u> -> '<b>$</b>',<br><u>QXZ</u> -> (<b>empty string</b>),<br><u>EUR</u> ->
     * '<b>&euro;</b>',<br>
     * <u>GBP</u> -> '<b>&#x20A4;</b>',<br><u>RUB</u> -> '<b>руб.</b>',<br><u>JPY</u> -> '<b>&yen;</b>', etc..
     */
    public static String getCurrencySymbol(String currencyCode){
        HashMap currencyLocaleMap = getCurrencyLocaleMapping();
        String symbol;
        if(currencyLocaleMap.get(currencyCode) != null)
        {
            Locale requiredLocale = (Locale)currencyLocaleMap.get(currencyCode);
            symbol = Currency.getInstance(requiredLocale).getSymbol(requiredLocale);
            symbol = symbol.contains("US") ? symbol.substring(symbol.length()-1) : symbol; //get just the symbol
        }
        else
            symbol = "";

        return symbol;
    }

    /**
     * @param fromValue The value in the currency to convert From
     * @param exchangeRate The exchange rate of 'currency to convert from/currency to convert to'
     * @return The value in the currency to be converted to. i.e: €100 will be converted to $117
     */
    public static Float convertToCurrency(Float fromValue, Float exchangeRate){
        Float toValue = fromValue * exchangeRate;
        BigDecimal rounder = new BigDecimal(Float.toString(toValue));
        rounder = rounder.setScale(2, BigDecimal.ROUND_HALF_UP);

        return rounder.floatValue();
    }

    /**
     * This method will refresh the ExchangeRateProvider every day at 8:00, 16:00 and 17:00
     */
    @Scheduled(cron = "0 0 8,16,17 * * *" )
    private static void refreshExchangeRateProvider(){
        try
        {
            provider = MonetaryConversions.getExchangeRateProvider();
        }
        catch(Exception e) {
            System.out.print("Could not refresh providers list.\nCaused by: " + e.getMessage());
        }
    }

    /**
     * This method will get all available locales, using java.util.locale library.
     * @return A HashMap object, containing the "Currency code-Locale" mappings found
     * as a key-value pair respectively.
     */
    private static HashMap<String, Locale> getCurrencyLocaleMapping() {
        HashMap<String, Locale> locales = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                String currency = Currency.getInstance(locale).getCurrencyCode();
                locales.put(currency, locale);
            }
            catch (IllegalArgumentException iae){
                //silently catch exceptions caused by unknown locales.
                //e.g: Locale.getDisplayCountry equals -> "." will throw IllegalArgumentException
            }
            catch (NullPointerException npe){
                System.out.println("Tried to call getInstance() on Null 'Locale' object");
            }
        }
        return locales;
    }

    /**
     * Private constructor of class. Only public methods and variables can be instantiated from a
     * static context. When the constructor is called for the first time, during bean initialization, it
     * will call refreshExchangeRateProvider() to update the provider.
     */
    private ExchangeRateProviderHandler(){

        refreshExchangeRateProvider();
    }
}
