package com.invoices.controller;

import com.invoices.domain.*;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author psoutzis
 * This Controller is responsible for the creation of an invoice.
 * It will set all the foreign keys of invoice before saving the actual invoice
 * to the database.
 * It will retrieve the appropriate records by using their id or enum.
 * The id or enum will be sent through a post request to
 * the controllers, with the home.js script.
 *
 * note: In the future user will only be required to insert the amount to charge
 * the customer. Vat value and total price will be auto-generated( Custody Charges )
 */

//TODO MAKE A THREAD-SAFE data struct TO STORE OBJECTS
    //Could send e.g: portfolioData first, and add a mutex semaphore to
    //portfolio mapping, that will be released in custody charges mapping
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

    private HashMap<String, Object> entities = new HashMap<>();

    @GetMapping("/create")
    public String createInvoice(Model model){
        //adding enum's values to the view, by passing them all as an array
        model.addAttribute("invoiceTypeValues", InvoiceType.values());
        model.addAttribute("isApplicableValues", IsApplicable.values());
        model.addAttribute("portfolios", portfolioService.getPortfolios());
        model.addAttribute("services", serviceProvidedService.getServicesProvided());
        model.addAttribute("currencies", currencyService.getAvailableCurrencies());
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
        bankAccount = bankAccountService.getRecord(bankAccount.getId());
        entities.put("bankAccount", bankAccount);

        return "createSuccess";
    }

    /**
     * It will save the parsed values to parameter object
     * @param portfolio will use the passed object from the server response body
     */
    @PostMapping(value="/portfolio")
    public String submitInvoice(@RequestBody Portfolio portfolio){
        portfolio = portfolioService.getRecord(portfolio.getId());
        entities.put("portfolio", portfolio);

        return "createSuccess";
    }

    /**
     * It will save the parsed values to parameter object
     * @param currency will use the passed object from the server response body
     */
    @PostMapping(value="/currency")
    public String submitInvoice(@RequestBody Currency currency){
        currency = currencyService.getRecord(currency.getCurrencyId());
        entities.put("currency", currency);

        return "createSuccess";
    }

    /**
     * It will save the parsed values to serviceProvided object
     * @param serviceProvided will use the passed object from the server response body
     */
    @PostMapping(value="/serviceProvided")
    public String submitInvoice(@RequestBody ServiceProvided serviceProvided){
        serviceProvided = serviceProvidedService.getRecord(serviceProvided.getId());
        entities.put("service", serviceProvided);

        return "createSuccess";
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

        entities.put("vat", vat);

        return "createSuccess";
    }

    /**
     * Method will get vat from body and add the object in Invoice object
     * Will check if same vat rate exists in database, if not, it will save it.
     * @param invoice will use the passed object from the server response body
     */
    @PostMapping(value = "/invoice")
    public String submitInvoice(@RequestBody Invoice invoice){
        CurrencyRates rates = currencyRatesService.getCurrentRates();

        invoice.setCurrencyRates(rates);
        invoice.setVat((Vat)entities.get("vat"));
        invoice.setServiceProvided((ServiceProvided)entities.get("service"));
        invoice.setCurrency((Currency)entities.get("currency"));
        invoice.setBankAccount((BankAccount)entities.get("bankAccount"));
        invoice.setPortfolio((Portfolio)entities.get("portfolio"));

        invoiceService.save(invoice); //insert current invoice to db
        entities.put("invoice", invoice);

        return "createSuccess";
    }

    /**
     * add the charges that will be made to the client
     * checks if an invoice has been parsed correctly first
     * @param custodyCharge will use the passed object from the server response body
     */
    @PostMapping(value = "/fee")
    public String submitInvoice(@RequestBody CustodyCharge custodyCharge){
        custodyCharge.setInvoice((Invoice)entities.get("invoice"));
        Float vatRate = vatService.getVatRate((Vat)entities.get("vat"));
        Float baseValue = custodyCharge.getChargeExcludingVat();
        Float vatCharge = custodyChargeService.calculateVatCharge(baseValue, vatRate);
        Float chargeInclVat = custodyChargeService.calculateTotalCharge(baseValue, vatCharge);

        custodyCharge.setVatCharge(vatCharge);
        custodyCharge.setChargeIncludingVat(chargeInclVat);

        custodyChargeService.save(custodyCharge);

        entities.clear();
        //release semaphore
        return "createSuccess";
    }

    @GetMapping(value = "/success")
    public String successMsg(){

        return "createSuccess";
    }
}
