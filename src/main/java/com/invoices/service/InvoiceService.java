package com.invoices.service;

import com.invoices.domain.*;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.enumerations.InvoiceFrequency;
import com.invoices.enumerations.InvoicePeriod;
import com.invoices.enumerations.InvoiceType;
import com.invoices.enumerations.IsApplicable;
import com.invoices.repository.InvoiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    private Date convertDate(String datestring) {
        Date date = null;
        //Ask what the preferred date format is.
        datestring = datestring.contains("/") ? datestring.replaceAll("/","-" ) : datestring;
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

    public void updateInvoice(Long id,
                                 UpdateInvoiceDTO dto,
                                 Portfolio portfolio,
                                 ServiceProvided serviceProvided,
                                 BankAccount bankAccount,
                                 Vat vat,
                                 CustodyCharge custodyCharge,
                                 CurrencyRates currencyRates){

        Invoice invoice = getInvoiceById(id);
        invoice.setInvoiceType(InvoiceType.valueOf(dto.getInvoiceType()));
        invoice.setVatExempt(IsApplicable.valueOf(dto.getVatExempt()));
        invoice.setReverseCharge(IsApplicable.valueOf(dto.getReverseCharge()));
        invoice.setFrequency(InvoiceFrequency.valueOf(dto.getFrequency()));
        invoice.setPeriod(InvoicePeriod.valueOf(dto.getPeriod()));
        invoice.setYear(Integer.valueOf(dto.getYear()));
        invoice.setInvoiceDate(convertDate(dto.getInvoiceDate()));
        invoice.setInvoiceNumber(dto.getInvoiceNumber());

        invoice.setPortfolio(portfolio);
        invoice.setServiceProvided(serviceProvided);
        invoice.setBankAccount(bankAccount);
        invoice.setVat(vat);
        invoice.setCustodyCharge(custodyCharge);
        invoice.setCurrencyRates(currencyRates);

        save(invoice);
    }
}
