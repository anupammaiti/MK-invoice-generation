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


    public CustodyCharge save(CustodyCharge custodyCharge){

        return custodyChargeRepo.save(custodyCharge);
    }
}
