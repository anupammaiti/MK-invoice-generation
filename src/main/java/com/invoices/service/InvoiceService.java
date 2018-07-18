package com.invoices.service;

import com.invoices.domain.Invoice;
import com.invoices.repository.CustodyChargeRepo;
import com.invoices.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the Invoice entity
 */
@Service
public class InvoiceService {

    @Autowired private InvoiceRepo invoiceRepo;

    public void save(Invoice invoice){

        invoiceRepo.save(invoice);
    }

    @Transactional
    public void deleteRecord(Long id){

        invoiceRepo.deleteById(id);
    }

    public List<Invoice> getInvoices(){
        //
        return invoiceRepo.findAll();
    }

    public Invoice getInvoiceById(Long id){
        //
        return invoiceRepo.getInvoiceById(id);
    }
}
