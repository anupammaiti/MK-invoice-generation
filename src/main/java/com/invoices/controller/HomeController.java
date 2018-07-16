package com.invoices.controller;

import com.invoices.domain.*;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired CustodyChargeService custodyChargeService;
    @Autowired BankAccountService bankAccountService;
    @Autowired InvoiceService invoiceService;
    @Autowired PortfolioService portfolioService;
    @Autowired ServiceProvidedService serviceProvidedService;
    @Autowired CurrencyService currencyService;
    @Autowired CurrencyRateService currencyRateService;
    @Autowired VatService vatService;

    @GetMapping("/")
    public String createInvoice(Model model){
        //adding enum's values to the view, by passing them all as an array
        model.addAttribute("invoiceTypeValues", InvoiceType.values());
        model.addAttribute("isApplicableValues", IsApplicable.values());
        model.addAttribute("portfolios", portfolioService.getPortfolios());
        model.addAttribute("services", serviceProvidedService.getAllServicesProvided());
        model.addAttribute("currencies", currencyService.getAvailableCurrencies());
        model.addAttribute("currencyRates", currencyRateService.getAvailableRates());
        model.addAttribute("vatRecords", vatService.getVatRecords());
        model.addAttribute("bankAccounts", bankAccountService.getBankAccounts());

        return "createInvoice";
    }

    /**
     * It will save the parsed values to parameter object
     * @param bankAccount will use the passed object from the server response body
     */
    @PostMapping(value="/bankAccount")
    public String submitInvoice(@RequestBody BankAccount bankAccount){

        bankAccountService.setBankAccount(bankAccountService.getRecord(bankAccount));
        return "empty";
    }

    /**
     * It will save the parsed values to parameter object
     * @param currencyRate will use the passed object from the server response body
     */
    @PostMapping(value="/currencyRate")
    public String submitInvoice(@RequestBody CurrencyRate currencyRate){
        currencyRate = currencyRateService.getCurrencyRateById(currencyRate.getCurrencyRateId());
        currencyRateService.setCurrencyRate(currencyRate);

        return "empty";
    }

    /**
     * It will save the parsed values to parameter object
     * @param portfolio will use the passed object from the server response body
     */
    @PostMapping(value="/portfolio")
    public String submitInvoice(@RequestBody Portfolio portfolio){

        portfolioService.setPortfolio(portfolio);
        return "empty";
    }

    /**
     * It will save the parsed values to parameter object
     * @param currency will use the passed object from the server response body
     */
    @PostMapping(value="/currency")
    public String submitInvoice(@RequestBody Currency currency){

        currencyService.setCurrency(currency);
        return "empty";
    }

    /**
     * It will save the parsed values to serviceProvided object
     * @param serviceProvided will use the passed object from the server response body
     */
    @PostMapping(value="/serviceProvided")
    public String submitInvoice(@RequestBody ServiceProvided serviceProvided){

        serviceProvidedService.setServiceProvided(serviceProvided);
        return "empty";
    }

    /**
     * It will save the vat object's values in a Vat object in VatService
     * @param vat will use the passed object from the server response body
     */
    @PostMapping(value = "/vat")
    public String checkVat(@RequestBody Vat vat){
        if(vat.getVatId()!=null)
            vat =  vatService.getVatById(vat.getVatId());
        else
            vatService.save(vat);
        vatService.setVat(vat);//Pass this vat object's values to a Vat object to the VatService bean

        return "empty";
    }

    /**
     * Method will get vat from body and add the object in Invoice object
     * Will check if same vat rate exists in database, if not, it will save it.
     * @param invoice will use the passed object from the server response body
     */
    @PostMapping(value = "/invoice")
    public String submitInvoice(@RequestBody Invoice invoice){

        //Set all the foreign keys of invoice before saving it to database
        //By retrieving the matching records from the database.
        invoice.setVat(vatService.getVat());
        invoice.setServiceProvided(serviceProvidedService.getRecord(serviceProvidedService.getServiceProvided()));
        invoice.setCurrency(currencyService.getRecord(currencyService.getCurrency()));
        invoice.setCurrencyRate(currencyRateService.getCurrencyRate());
        invoice.setPortfolio(portfolioService.getRecord(portfolioService.getPortfolio()));
        invoice.setBankAccount(bankAccountService.getBankAccount());

        invoiceService.save(invoice); //insert current invoice to db
        invoiceService.setInvoice(invoice); //pass this invoice object to invoiceService

        return "empty";
    }

    /**
     * add the charges that will be made to the client
     * checks if an invoice has been parsed correctly first
     * @param custodyCharge will use the passed object from the server response body
     */
    @PostMapping(value = "/fee")
    public String submitInvoice(@RequestBody CustodyCharge custodyCharge){
        custodyCharge.setInvoice(invoiceService.getInvoice());
        if(custodyCharge.getInvoice() != null)
            custodyChargeService.save(custodyCharge);

        return "redirect:/empty.html"; //TODO FIX THIS REDIRECT. CURRENTLY CAUSING AN ERROR
    }

    /*@GetMapping(value = "/success")
    public String successMsg(){
        return "empty";
    }*/
}
