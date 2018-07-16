package com.invoices.service;

import com.invoices.domain.CurrencyRate;
import com.invoices.repository.CurrencyRateRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyRateService {
    @Autowired
    private CurrencyRateRepo currencyRateRepo;

    @Getter@Setter
    private CurrencyRate currencyRate;

    public CurrencyRate getCurrencyRateById(Long id){
        return currencyRateRepo.getCurrencyRateByCurrencyRateId(id);
    }

   public List<CurrencyRate> getAvailableRates(){
        return currencyRateRepo.findAll();
   }

    public CurrencyRate save(CurrencyRate currencyRate){

        return currencyRateRepo.save(currencyRate);
    }
}
