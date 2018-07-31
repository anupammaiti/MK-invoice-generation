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
import org.springframework.web.bind.annotation.*;

/**
 * @author psoutzis
 * This Controller is responsible for the creation of an invoice.
 * It will set all the foreign keys of invoice before saving the actual invoice
 * to the database.
 * It will retrieve the appropriate records by using their id or enum.
 * The id or enum will be sent through a post request to
 * the controllers, with the create.js script.
 *
 * note: In the future user will only be required to insert the amount to charge
 * the customer. Vat value and total price will be auto-generated( Custody Charges )
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

    @PostMapping(value = "/generate-invoice")
    @ResponseBody
    public void generateInvoice(@RequestBody InvoiceDTO invoiceDTO) {

        IsApplicable vatApplicable = IsApplicable.valueOf(invoiceDTO.getVatApplicable());
        Portfolio portfolio = portfolioService.getRecord(invoiceDTO.getPortfolio());
        BankAccount bankAccount = bankAccountService.getRecord(invoiceDTO.getBankAccount());
        Currency currency = currencyService.getRecord(invoiceDTO.getFromCurrency());
        Currency toCurrency = currencyService.getRecord(invoiceDTO.getToCurrency());
        ServiceProvided service = serviceProvidedService.getRecord(invoiceDTO.getServiceProvided());
        Vat vat = vatService.determineVat(vatApplicable, invoiceDTO.getVat());

        CurrencyRates currencyRates = currencyRatesService.generateExchangeRate(currency, toCurrency);
        CustodyCharge custodyCharge = custodyChargeService.generateCustodyCharge(
                invoiceDTO.getBaseCharge(),
                vat.getVatRate());

        Invoice invoice = new Invoice(null, InvoiceType.valueOf(invoiceDTO.getInvoiceType()),
                invoiceDTO.getInvoiceNumber(), InvoiceFrequency.valueOf(invoiceDTO.getFrequency()),
                InvoicePeriod.valueOf(invoiceDTO.getPeriod()), invoiceDTO.getInvoiceDate(),
                invoiceDTO.getYear(), IsApplicable.valueOf(invoiceDTO.getReverseCharge()),
                IsApplicable.valueOf(invoiceDTO.getVatExempt()), service, bankAccount, currency,
                currencyRates, vat, portfolio, custodyCharge);

        invoiceService.save(invoice);
    }
}
