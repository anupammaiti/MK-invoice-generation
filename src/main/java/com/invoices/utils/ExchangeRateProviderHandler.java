package com.invoices.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class ExchangeRateProviderHandler {

    public static ExchangeRateProvider provider;

    private ExchangeRateProviderHandler(){

        refreshExchangeRateProvider();
    }

    @Scheduled(cron = "0 0 8,12,14,16,17 * * *" )
    protected static void refreshExchangeRateProvider(){
        try
        {
            provider = MonetaryConversions.getExchangeRateProvider();
        }
        catch(Exception e)
        {
            System.out.print("Could not refresh providers list.\nReason: "
                    + e.getMessage()+"\nStack Trace: ");
            e.printStackTrace();
        }
    }

    public static String getCurrencySymbol(String currencyCode){
        Map currencyLocaleMap = getCurrencyLocaleMap();
        String symbol;
        if(currencyLocaleMap.get(currencyCode) != null)
        {
            Locale requiredLocale = (Locale)currencyLocaleMap.get(currencyCode);
            symbol = Currency.getInstance(requiredLocale).getSymbol(requiredLocale);
            symbol = symbol.contains("US") ? symbol.substring(symbol.length()-1) : symbol;
        }
        else
            symbol = null;

        return symbol;
    }

    public static Float convertToCurrency(Float fromValue, Float exchangeRate){
        Float toValue = fromValue * exchangeRate;
        BigDecimal rounder = new BigDecimal(Float.toString(toValue));
        rounder = rounder.setScale(2, BigDecimal.ROUND_HALF_UP);

        return rounder.floatValue();
    }

    private static Map<String, Locale> getCurrencyLocaleMap() {
        Map<String, Locale> map = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try
            {
                String currency = Currency.getInstance(locale).getCurrencyCode();
                map.put(currency, locale);
            }
            catch (Exception e){
                // skip strange locale
            }
        }
        return map;
    }
}
