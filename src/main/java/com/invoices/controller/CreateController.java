package com.invoices.controller;

import com.invoices.domain.*;
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

/**
 * @author psoutzis
 * This Controller is responsible for the creation of an invoice, based on
 * user input.
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

    /**
     * @param model will all required objects/attributes to the view
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

        return "create/create-invoice";
    }

    /**
     * This method will listen for a post request and will receive all relevant attributes for the
     * creation of an invoice in JSON format.
     * The method will finally save the invoice to the database.
     * @param invoiceDTO a Data Transfer Object (DTO), to hold all necessary information to create an invoice.
     */
    @PostMapping(value = "/generate-invoice")
    @ResponseBody
    public void generateInvoice(@RequestBody InvoiceDTO invoiceDTO) {

        IsApplicable vatApplicable = IsApplicable.valueOf(invoiceDTO.getVatApplicable());
        Portfolio portfolio = portfolioService.getRecord(invoiceDTO.getPortfolio());
        BankAccount bankAccount = bankAccountService.getRecord(invoiceDTO.getBankAccount());
        ServiceProvided service = serviceProvidedService.getRecord(invoiceDTO.getServiceProvided());
        Vat vat = vatService.determineVat(vatApplicable, invoiceDTO.getVat());

        Currency fromCurrency = currencyService.getRecord(invoiceDTO.getFromCurrency());
        Currency toCurrency = currencyService.getRecord(invoiceDTO.getToCurrency());
        CurrencyRates currencyRates = currencyRatesService.generateExchangeRate(
                fromCurrency, toCurrency,new CurrencyRates(), null);
        CustodyCharge custodyCharge = custodyChargeService.generateCustodyCharge(
                invoiceDTO.getBaseCharge(), vat.getVatRate(),new CustodyCharge());

        Invoice invoice = new Invoice(null, InvoiceType.valueOf(invoiceDTO.getInvoiceType()),
                invoiceDTO.getInvoiceNumber(), InvoiceFrequency.valueOf(invoiceDTO.getFrequency()),
                InvoicePeriod.valueOf(invoiceDTO.getPeriod()), invoiceDTO.getInvoiceDate(),
                invoiceDTO.getYear(), IsApplicable.valueOf(invoiceDTO.getReverseCharge()),
                IsApplicable.valueOf(invoiceDTO.getVatExempt()), service, bankAccount,
                currencyRates, vat, portfolio, custodyCharge);

        invoiceService.save(invoice);
    }
}
