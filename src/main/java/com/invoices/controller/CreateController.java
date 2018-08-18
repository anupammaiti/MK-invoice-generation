package com.invoices.controller;

import com.invoices.domain.*;
import com.invoices.domain.Currency;
import com.invoices.dto.InvoiceDTO;
import com.invoices.enumerations.InvoiceFrequency;
import com.invoices.enumerations.InvoicePeriod;
import com.invoices.enumerations.InvoiceType;
import com.invoices.enumerations.IsApplicable;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * This Controller is responsible for the creation of an invoice, based on
 * user input.
 * @author psoutzis
 */

@Controller
public class CreateController {

    @Autowired CustodyChargeService custodyChargeService;
    @Autowired BankAccountService bankAccountService;
    @Autowired InvoiceService invoiceService;
    @Autowired PortfolioService portfolioService;
    @Autowired ServiceProvidedService serviceProvidedService;
    @Autowired CurrencyService currencyService;
    @Autowired CurrencyRatesService currencyRatesService;
    @Autowired VatService vatService;
    @Autowired InvoiceStatusService invoiceStatusService;

    /**
     * @param model will add all the required objects/attributes for the app to function, to the view
     * @return the page where user can create an invoice
     */
    @GetMapping("/select/create")
    public String createInvoice(Model model){
        model.addAttribute("invoiceTypeValues", InvoiceType.values());
        model.addAttribute("isApplicableValues", IsApplicable.values());
        model.addAttribute("frequencyValues", InvoiceFrequency.values());
        model.addAttribute("periodValues", InvoicePeriod.values());
        model.addAttribute("portfolios", portfolioService.getPortfolios());
        model.addAttribute("services", serviceProvidedService.getServicesProvided());
        model.addAttribute("currencies", currencyService.getAvailableCurrencies());
        model.addAttribute("vatRecords", vatService.getVatRecords());
        model.addAttribute("bankAccounts", bankAccountService.getBankAccounts());

        List<Invoice> invoices = invoiceService.getInvoices();
        List<String> invoiceNumberList = new ArrayList<>();
        //Get all invoice numbers in a list of Strings.
        invoices.forEach(inv -> invoiceNumberList.add(inv.getInvoiceNumber()));
        //Concatenate all Strings from list and separate them with a coma (,)
        String invoiceNumbers = String.join(",", invoiceNumberList);
        //add the created String to the view and use it to prevent user from entering a duplicate.
        model.addAttribute("invoiceNumberList", invoiceNumbers);

        return "create/create-invoice";
    }

    /**
     * This method will listen for a post request and will receive all relevant attributes for the
     * creation of an invoice in JSON format.
     * The method will finally save the invoice to the database.
     * @param dto a Data Transfer Object (DTO), to hold all necessary information to create an invoice.
     */
    @PostMapping(value = "/generate-invoice")
    @ResponseBody
    public void generateInvoice(@RequestBody InvoiceDTO dto) {

        Portfolio portfolio = portfolioService.getRecord(dto.getPortfolio());
        BankAccount bankAccount = bankAccountService.getRecord(dto.getBankAccount());
        ServiceProvided service = serviceProvidedService.getRecord(dto.getServiceProvided());
        Vat vat;
        //If VAT has to be applied, get the appropriate record if it exists (or save and return it)
        if(vatService.determineVatRate(dto.getVatApplicable(),dto.getVatExempt() ,dto.getReverseCharge()))
            vat = vatService.getRecordAndSaveIfNotExists(dto.getVatRate());
        else
            vat = vatService.getRecordWithRateOfZero();

        Currency fromCurrency = currencyService.getRecord(dto.getFromCurrency());
        Currency toCurrency = currencyService.getRecord(dto.getToCurrency());
        CurrencyRates currencyRates = currencyRatesService.generateExchangeRate(
                fromCurrency, toCurrency,new CurrencyRates(), null);
        CustodyCharge custodyCharge = custodyChargeService.generateCustodyCharge(
                dto.getBaseCharge(), vat.getVatRate(),new CustodyCharge());

        //Finally, construct the appropriate Invoice-type object
        Invoice invoice = new Invoice(null, InvoiceType.valueOf(dto.getInvoiceType()),
                dto.getInvoiceNumber(), InvoiceFrequency.valueOf(dto.getFrequency()),
                InvoicePeriod.valueOf(dto.getPeriod()), dto.getInvoiceDate(),
                dto.getYear(), IsApplicable.valueOf(dto.getReverseCharge()),
                IsApplicable.valueOf(dto.getVatExempt()), service, bankAccount,
                currencyRates, vat, portfolio, custodyCharge, invoiceStatusService.getUnsentUnpaid());


        invoiceService.save(invoice);
    }
}
