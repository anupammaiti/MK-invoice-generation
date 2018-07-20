package com.invoices.service;

import com.invoices.domain.Invoice;
import com.invoices.domain.InvoiceTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is a specialisation component that will be responsible for initialising
 * an InvoiceTemplate object with all the appropriate values, so an invoice
 * can be output as a PDF document.
 */
@Service
public class PdfService {

    /*
    //TODO Simplify accessor methods
    public InvoiceTemplate generateTemplate(Invoice invoice){
        String companyName = invoice.getPortfolio().getClientCompanyInfo().getName();
        String address = invoice.getPortfolio().getClientCompanyInfo().getAddress();
        String postcode = invoice.getPortfolio().getClientCompanyInfo().getPostcode();
        String city = invoice.getPortfolio().getClientCompanyInfo().getCity();
        String country = invoice.getPortfolio().getClientCompanyInfo().getCompanyLocation().getCountry();
        String vatNumber = invoice.getPortfolio().getClientCompanyInfo().getVatNumber();
        //Date invoiceDate = invoice.getInvoiceDate();
        String invoiceNumber = invoice.getInvoiceNumber();
        Float chargeExcludingVat = invoice.getCustodyCharge().getChargeExcludingVat();
        Float chargeIncludingVat = invoice.getCustodyCharge().getChargeIncludingVat();
        Float vatCharge = invoice.getCustodyCharge().getVatCharge();
        Float vatRate = invoice.getVat().getVatRate();

        Float currencyRate = invoice.getCurrencyRates().getExchangeRate();

        InvoiceTemplate template = new InvoiceTemplate(companyName, address, postcode, city, country,
                vatNumber, invoiceDate, invoiceNumber, chargeExcludingVat, chargeIncludingVat, vatCharge,
                vatRate, currencyRate);

        return template;
    }
*/
}
