package com.invoices.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

@Component
public class ExchangeRateProviderHandler {

    public static ExchangeRateProvider provider = MonetaryConversions.getExchangeRateProvider();

    @Scheduled(cron = "0 0 8,12,14,16,17 * * *" )
    protected static void refreshXrateProvider(){

        provider = MonetaryConversions.getExchangeRateProvider();
    }
}
