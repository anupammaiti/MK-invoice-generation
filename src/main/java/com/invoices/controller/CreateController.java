package com.invoices.controller;

import com.invoices.domain.*;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

        return "create/createInvoice";
    }

    /**
     * It will save the parsed object in the controller's entity hashmap
     * @param bankAccount will use the passed object from the server response body
     */
    @PostMapping(value="/bankAccount")
    @ResponseBody
    public void submitInvoice(@RequestBody BankAccount bankAccount){
        bankAccount = bankAccountService.getRecord(bankAccount.getId());
        entities.put("bankAccount", bankAccount);
    }

    /**
     * It will save the parsed object in the controller's entity hashmap
     * @param portfolio will use the passed object from the server response body
     */
    @PostMapping(value="/portfolio")
    @ResponseBody
    public void submitInvoice(@RequestBody Portfolio portfolio){
        portfolio = portfolioService.getRecord(portfolio.getId());
        entities.put("portfolio", portfolio);
    }

    /**
     * It will save the parsed values to the controller's entity hashmap
     * @param currency will use the passed object from the server response body
     */
    @PostMapping(value="/currency")
    @ResponseBody
    public void submitInvoice(@RequestBody Currency currency){
        currency = currencyService.getRecord(currency.getCurrencyId());
        entities.put("currency", currency);
    }

    /**
     * It will save the parsed object in the controller's entity hashmap
     * @param serviceProvided will use the passed object from the server response body
     */
    @PostMapping(value="/serviceProvided")
    @ResponseBody
    public void submitInvoice(@RequestBody ServiceProvided serviceProvided){
        serviceProvided = serviceProvidedService.getRecord(serviceProvided.getId());
        entities.put("service", serviceProvided);
    }

    /**
     * It will save the vat object in the controller's entity hashmap
     * @param vat will use the passed object from the server response body
     */
    @PostMapping(value = "/vat")
    @ResponseBody
    public void checkVat(@RequestBody Vat vat){
        if(vat.getVatId()!=null && vat.getIsApplicable()!=IsApplicable.NO)
            vat =  vatService.getVatById(vat.getVatId());

        else if(vat.getIsApplicable() == IsApplicable.NO){
            vat = vatService.getRecordByVatRate(0F);
        }

        else if(vat.getVatId()==null)
            vatService.save(vat);

        entities.put("vat", vat);
    }

    /**
     * It will save the currencyRate object in the controller's entity hashmap
     * @param currencyRate will use the passed object from the server response body
     */
    @PostMapping(value = "/exchangeRate")
    @ResponseBody
    public void checkVat(@RequestBody CurrencyRates currencyRate){
        Currency from = currencyService.getRecord(currencyRate.getFromCurrencyId());
        Currency to = currencyService.getRecord(currencyRate.getToCurrencyId());

        currencyRate.setFromCurrency(from);
        currencyRate.setToCurrency(to);

        entities.put("currencyRate", currencyRate);
    }

    /**
     * add the charges that will be made to the client
     * checks if an invoice has been parsed correctly first
     * @param custodyCharge will use the passed object from the server response body
     */
    @PostMapping(value = "/fee")
    @ResponseBody
    public void submitInvoice(@RequestBody CustodyCharge custodyCharge){
        Long vatRateId = custodyCharge.getVatRateId();
        Vat v = vatService.getVatById(vatRateId);
        Float vatRate = v.getVatRate();
        Float baseValue = custodyCharge.getChargeExcludingVat();
        Float vatCharge = custodyChargeService.calculateVatCharge(baseValue, vatRate);
        Float chargeInclVat = custodyChargeService.calculateTotalCharge(baseValue, vatCharge);

        custodyCharge.setVatCharge(vatCharge);
        custodyCharge.setChargeIncludingVat(chargeInclVat);

        custodyChargeService.save(custodyCharge);
        entities.put("custodyCharge", custodyCharge);
    }

    /**
     * Method will get vat from body and add the object in Invoice object
     * Will check if same vat rate exists in database, if not, it will save it.
     * @param invoice will use the passed object from the server response body
     */
    @PostMapping(value = "/invoice")
    @ResponseBody
    public void submitInvoice(@RequestBody Invoice invoice){
        CurrencyRates exchangeRate = (CurrencyRates)entities.get("currencyRate");
        exchangeRate = currencyRatesService.getCurrentRateAndSave(exchangeRate);

        invoice.setVat((Vat)entities.get("vat"));
        invoice.setServiceProvided((ServiceProvided)entities.get("service"));
        invoice.setCurrency((Currency)entities.get("currency"));
        invoice.setBankAccount((BankAccount)entities.get("bankAccount"));
        invoice.setPortfolio((Portfolio)entities.get("portfolio"));
        invoice.setCustodyCharge((CustodyCharge)entities.get("custodyCharge"));
        invoice.setCurrencyRates(exchangeRate);

        invoiceService.save(invoice); //finally insert invoice to db
        entities.clear(); //remove all objects from hashmap
    }

    @GetMapping(value = "/success")
    public String successMsg(){

        return "create/createSuccess";
    }
}
