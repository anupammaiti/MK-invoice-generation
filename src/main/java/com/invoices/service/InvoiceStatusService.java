package com.invoices.service;

import com.invoices.domain.InvoiceStatus;
import com.invoices.enumerations.IsApplicable;
import com.invoices.repository.InvoiceStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is annotated as a service.
 * It is the service bean for the InvoiceStatus entity
 * @author psoutzis
 */
@Service
public class InvoiceStatusService {

    @Autowired private InvoiceStatusRepo invoiceStatusRepo;

    /**
     * Method will return the status of the invoice (e.g. Sent but Unpaid, or Sent and Paid, etc..)
     * @param sent Determines if the invoice was sent to the client.
     * @param paid Determines if the invoice was paid.
     * @return The status of the invoice
     */
    public InvoiceStatus determineStatus(IsApplicable sent, IsApplicable paid){
        if(sent == IsApplicable.NO && paid==IsApplicable.NO)
            return getUnsentUnpaid();
        else if(sent == IsApplicable.YES && paid == IsApplicable.NO)
            return getSentUnpaid();
        else
            return getSentPaid();
    }

    /**
     * Method returns status of the invoice as unpaid and unsent.
     * @return The record who has paid and sent both set as IsApplicable.NO.
     */
    public InvoiceStatus getUnsentUnpaid(){

        return invoiceStatusRepo.getInvoiceStatusBySentAndPaid(IsApplicable.NO,IsApplicable.NO);
    }

    /**
     * Method returns status of the invoice as sent, but still unpaid.
     * @return The record who has sent as IsApplicable.YES and paid set as IsApplicable.NO.
     */
    private InvoiceStatus getSentUnpaid(){

        return invoiceStatusRepo.getInvoiceStatusBySentAndPaid(IsApplicable.YES,IsApplicable.NO);
    }

    /**
     * Method returns status of the invoice that was sent and paid for.
     * @return The record who has paid and sent both set as IsApplicable.YES
     */
    private InvoiceStatus getSentPaid(){

        return invoiceStatusRepo.getInvoiceStatusBySentAndPaid(IsApplicable.YES,IsApplicable.YES);
    }
}