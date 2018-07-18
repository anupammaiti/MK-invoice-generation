package com.invoices.service;

import com.invoices.domain.CustodyCharge;
import com.invoices.repository.CustodyChargeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the CustodyCharge entity
 */
@Service
public class CustodyChargeService {

    @Autowired
    private CustodyChargeRepo custodyChargeRepo;

    /**
     * @param baseValue the custody charge excluding VAT
     * @param vatRate the rate of VAT
     * @return the "pure" amount of VAT to be paid
     */
    public Float calculateVatCharge(float baseValue, float vatRate){

        return baseValue*vatRate;
    }

    /**
     * @param baseValue the custody charge excluding VAT
     * @param vatValue the "pure" amount of VAT to be paid
     * @return the total fee to be paid
     */
    public Float calculateTotalCharge(float baseValue, float vatValue){

        return baseValue+vatValue;
    }

    public CustodyCharge save(CustodyCharge custodyCharge){

        return custodyChargeRepo.save(custodyCharge);
    }
}
