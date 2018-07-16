package com.invoices.service;

import com.invoices.domain.CustodyCharge;
import com.invoices.repository.CustodyChargeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustodyChargeService {

    @Autowired
    private CustodyChargeRepo custodyChargeRepo;

    /*@Autowired
    public CustodyChargeService(CustodyChargeRepo custodyChargeRepo) {
        this.custodyChargeRepo = custodyChargeRepo;
    }*/

    public CustodyCharge save(CustodyCharge custodyCharge){

        return custodyChargeRepo.save(custodyCharge);
    }
}
