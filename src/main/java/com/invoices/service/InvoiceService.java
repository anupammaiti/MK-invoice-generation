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
 * This class is annotated as a service.
 * It is the service bean for the Invoice entity
 * @author psoutzis
 */
@Service
public class InvoiceService {

    @Autowired private InvoiceRepo invoiceRepo;

    /**
     * @param invoice The Invoice object to 'save' to the database.
     */
    public void save(Invoice invoice){

        invoiceRepo.save(invoice);
    }

    /**
     * This method will delete a record from the database.
     * @param id The primary key of the record to delete from the database
     */
    @Transactional
    public void deleteRecord(Long id){

        invoiceRepo.deleteById(id);
    }

    /**
     * @return A collection of all the Invoice objects that exist in the database
     */
    public List<Invoice> getInvoices(){

        return invoiceRepo.findAll();
    }

    /**
     * @param id The primary key of the record to retrieve from the database
     * @return The record with a primary key that is equal to the argument
     */
    public Invoice getInvoiceById(Long id){

        return invoiceRepo.getInvoiceById(id);
    }

    /**
     * @param datestring The String representing an ISO date
     * @return a Date-type object, formed from the String passed as a parameter. The format of the Date returned
     * will be: <b><i>'yyyy-mm-dd'</i></b>
     */
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

    /**
     * This method will set all fields of an Invoice and save it to the database. This method is used to
     * <b>'update'</b> an existing Invoice record.
     * @param id The primary key of the record to update.
     * @param dto The DTO containing all fields that need to be updated.
     * @param portfolio The new portfolio that the invoice will have.
     * @param serviceProvided The new ServiceProvided that the invoice will have
     * @param bankAccount The new BankAccount that the invoice will have.
     * @param vat The new VAT rate that the invoice will have.
     * @param custodyCharge The new CustodyCharges that the invoice will have.
     * @param currencyRates The new exchange rate and currency (ies) that the invoice will have.
     */
    public void updateInvoice(Long id,
                              UpdateInvoiceDTO dto,
                              Portfolio portfolio,
                              ServiceProvided serviceProvided,
                              BankAccount bankAccount,
                              Vat vat,
                              CustodyCharge custodyCharge,
                              CurrencyRates currencyRates,
                              InvoiceStatus status){

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
        invoice.setInvoiceStatus(status);

        save(invoice);
    }
}
