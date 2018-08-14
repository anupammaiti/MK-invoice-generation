package com.invoices.service;

import com.invoices.domain.Vat;
import com.invoices.enumerations.IsApplicable;
import com.invoices.repository.VatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the Vat entity
 */
@Service
public class VatService {

    @Autowired private VatRepo vatRepo;

    /**
     * This method is used when updating or creating an invoice. It checks if a record with the vat rate passed as an
     * argument exists in the database and returns it. If it doesn't it inserts a new record for that rate.
     * @param rate The value of the rate to get from db (or insert as a new record for it, if it doesn't exist).
     * @return The Vat record containing the rate requested.
     */
    public Vat getRecordAndSaveIfNotExists(Float rate){
        Vat vat = getRecordWithRateOf(rate);

        return (vat != null
                ? vat
                : save( new Vat(null, IsApplicable.YES, rate )));
    }

    public boolean determineVatRate(String isApplicable, String isExempt, String chargeReversed) {
        IsApplicable vatApplicable = IsApplicable.valueOf(isApplicable);
        IsApplicable vatExempt = IsApplicable.valueOf(isExempt);
        IsApplicable reverseCharge = IsApplicable.valueOf(chargeReversed);

        return !(isVatApplicable(vatExempt) || isVatApplicable(reverseCharge) ||!isVatApplicable(vatApplicable));
    }

    private boolean isVatApplicable(IsApplicable isApplicable){

        return isApplicable == IsApplicable.YES;
    }

    /**
     * Method will insert the parsed Vat object to the database
     * @param vat the Vat object to insert to database as a record
     * @return the Vat object that was just inserted to db (and now has an autogenerated ID)
     */
    private Vat save(Vat vat){

        return vatRepo.save(vat);
    }

    /**
     * @return all the Vat records from the database
     */
    public List<Vat> getVatRecords(){

        return vatRepo.findAll();
    }

    /**
     * @param id the id of vat record to fetch
     * @return a Vat record with a unique id, identical to the one passed as an argument
     */
    private Vat getRecord(Long id){

        return vatRepo.findVatByVatId(id);
    }


    /**
     * @return the record from the database that has a rate of 0.0%
     */
    public Vat getRecordWithRateOfZero(){

        return vatRepo.findVatByVatRate(0F);
    }

    private Vat getRecordWithRateOf(Float rate){
        try {
            return vatRepo.findByAccurateVatRate(rate-0.0001f, rate+0.0001f);
        }
        catch(NullPointerException e){
            System.out.println("Record with rate of '"+rate+"' does not exist in database.");
            return null;
        }
    }

}
