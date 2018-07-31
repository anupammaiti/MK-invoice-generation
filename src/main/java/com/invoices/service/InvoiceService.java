package com.invoices.service;

import com.invoices.domain.*;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.enumerations.InvoiceType;
import com.invoices.repository.CustodyChargeRepo;
import com.invoices.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

        return invoiceRepo.findAll();
    }

    public Invoice getInvoiceById(Long id){

        return invoiceRepo.getInvoiceById(id);
    }

    public Date convertDate(String datestring) {
        Date date = null;
        //Ask what the preferred date format is.
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = sdf.parse(datestring);
        }
        catch(ParseException p){
            p.printStackTrace();
            System.out.println("Could not parse date");
        }

        return date;
    }

    public Invoice updateInvoice(UpdateInvoiceDTO updateInvoiceDTO,
                                 Portfolio portfolio/*,
                                 BankAccount bankAccount,
                                 ServiceProvided serviceProvided,
                                 CurrencyRates currencyRates,
                                 Vat vat,
                                 CustodyCharge custodyCharge,
                                 Currency currency*/){

        Invoice invoice = getInvoiceById(updateInvoiceDTO.getInvoiceId());
        invoice.setInvoiceType(InvoiceType.valueOf(updateInvoiceDTO.getInvoiceType()));
        invoice.setPortfolio(portfolio);

        return invoice;
    }
}
