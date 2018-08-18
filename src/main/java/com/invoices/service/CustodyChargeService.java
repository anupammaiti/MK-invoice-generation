package com.invoices.service;

import com.invoices.domain.CustodyCharge;
import com.invoices.repository.CustodyChargeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is annotated as a service.
 * It is the service bean for the CustodyCharge entity
 * @author psoutzis
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
    private Float calculateVatCharge(Float baseValue, Float vatRate){

        return baseValue*vatRate;
    }

    /**
     * @param baseValue the custody charge excluding VAT
     * @param vatCharge the "pure" amount of VAT to be paid
     * @return the total fee to be paid
     */
    private Float calculateTotalCharge(Float baseValue, Float vatCharge){

        return baseValue+vatCharge;
    }

    /**
     *
     * @param baseValue The amount of money that the invoice addressee has to pay, without VAT.
     * @param vatRate The rate of VAT.
     * @param custodyCharge A CustodyCharge object (new or to be updated).
     * @return The created (or updated) CustodyCharge object.
     */
    public CustodyCharge generateCustodyCharge(Float baseValue, Float vatRate, CustodyCharge custodyCharge){
        Float vatCharge = calculateVatCharge(baseValue, vatRate);
        Float total = calculateTotalCharge(baseValue, vatCharge);
        CustodyCharge generatedCustodyCharge = new CustodyCharge(
                custodyCharge.getCustodyChargeId(),vatCharge, total, baseValue);

        return save(generatedCustodyCharge);
    }

    /**
     * Method will insert a record in the <i>"custody_charges"</i> table(or update an existing record).
     * @param custodyCharge The CustodyCharge object to 'save'.
     * @return The CustodyCharge object, representing the newly inserted/updated CustodyCharge record.
     */
    private CustodyCharge save(CustodyCharge custodyCharge){

        return custodyChargeRepo.save(custodyCharge);
    }
}
