package com.invoices.controller;

import com.invoices.domain.*;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.enumerations.InvoiceFrequency;
import com.invoices.enumerations.InvoicePeriod;
import com.invoices.enumerations.InvoiceType;
import com.invoices.enumerations.IsApplicable;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a controller responsible for managing the HTTP requests about invoice updates.
 * Updates made to an invoice, will be automatically propagated to all the respective tables in the database,
 * by using "Cascading" of the Hibernate framework.
 * @author psoutzis, email: soutzis.petros@gmail.com
 */

@Controller
public class UpdateController {

    @Autowired InvoiceService invoiceService;
    @Autowired PortfolioService portfolioService;
    @Autowired ServiceProvidedService serviceProvidedService;
    @Autowired CurrencyService currencyService;
    @Autowired BankAccountService bankAccountService;
    @Autowired VatService vatService;
    @Autowired CompanyLocationService companyLocationService;
    @Autowired ClientCompanyInfoService clientCompanyInfoService;
    @Autowired CustodyChargeService custodyChargeService;
    @Autowired CurrencyRatesService currencyRatesService;
    @Autowired InvoiceStatusService invoiceStatusService;

    /**
     * @param id the id of the invoice to update.
     * @param model is the model that will hold data to be displayed by the view
     * @return the page where the user will make their invoice-changes at
     * <b>WARNING</b>: Html and JavaScript code depends on the NAMING of MODEL VARIABLES. Renaming could lead to bugs.
     */
    @PostMapping(value = "/find/update")
    public String findToUpdate(@RequestParam ("id") String id, Model model){
        Long invoiceId = Long.valueOf(id);
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        model.addAttribute("invoiceTypeValues", InvoiceType.values());
        model.addAttribute("isApplicableValues", IsApplicable.values());
        model.addAttribute("frequencyValues", InvoiceFrequency.values());
        model.addAttribute("periodValues", InvoicePeriod.values());
        model.addAttribute("portfolios", portfolioService.getPortfolios());
        model.addAttribute("services", serviceProvidedService.getServicesProvided());
        model.addAttribute("currencies", currencyService.getAvailableCurrencies());
        model.addAttribute("vatRecords", vatService.getVatRecords());
        model.addAttribute("bankAccounts", bankAccountService.getBankAccounts());
        model.addAttribute("countries", companyLocationService.getCountriesList());
        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());

        List<Invoice> allInvoices = invoiceService.getInvoices();
        List<String> invoiceNumberList = new ArrayList<>();
        allInvoices.forEach(inv -> invoiceNumberList.add(inv.getInvoiceNumber()));
        String invoiceNumbers = String.join(",", invoiceNumberList);

        //add all the existing invoice numbers, to prevent user from entering a duplicate
        model.addAttribute("invoiceNumberList", invoiceNumbers);

        return "update/update-attributes";
    }

    /**
     * Method that listens for the HTTP request to apply the selected updates
     * @param dto a Data Transfer Object (DTO) to hold all data
     * relevant to the incoming update request.
     */
    @PostMapping("/find/update/execute")
    @ResponseBody
    public void executeUpdate(@RequestBody UpdateInvoiceDTO dto){
        //Get the invoice that the user wants to update
        Invoice invoice = invoiceService.getInvoiceById(dto.getInvoiceId());
        Portfolio portfolio = portfolioService.getRecord(Long.valueOf(dto.getPortfolio()));

        IsApplicable sent = IsApplicable.valueOf(dto.getSent());
        IsApplicable paid = IsApplicable.valueOf(dto.getPaid());
        InvoiceStatus status = invoiceStatusService.determineStatus(sent, paid);

        //if company id is null, don't update anything about company
        if( dto.getCompanyId() != null) {
            //The company object before it was updated, to check for changes
            ClientCompanyInfo tempCompany = clientCompanyInfoService.getRecord(dto.getCompanyId());
            //If location does not exist in database, then save it
            CompanyLocation location = companyLocationService.getRecordAndSave(dto.getCompanyCountry());
            tempCompany.setClient(portfolio.getClient());
            tempCompany.setCompanyLocation(location);
            ClientCompanyInfo company = clientCompanyInfoService
                    .detectChangesAndApply(dto,tempCompany);
            //set updated (or not) company to portfolio
            portfolio.setClientCompanyInfo(company);
        }
        Vat vat;
        if(vatService.determineVatRate(dto.getVatApplicable(),dto.getVatExempt() ,dto.getReverseCharge()))
            vat = vatService.getRecordAndSaveIfNotExists(dto.getVatRate());
        else
            vat = vatService.getRecordWithRateOfZero();

        ServiceProvided serviceProvided = serviceProvidedService.getRecord(Long.valueOf(dto.getServiceProvided()));
        BankAccount bankAccount = bankAccountService.getRecord(Long.valueOf(dto.getBankAccount()));
        CustodyCharge custodyCharge = custodyChargeService.generateCustodyCharge(
                Float.valueOf(dto.getCustodyCharge()), vat.getVatRate(),invoice.getCustodyCharge());
        Currency fromCurrency = currencyService.getRecord(Long.valueOf(dto.getFromCurrency()));
        Currency toCurrency = currencyService.getRecord(Long.valueOf(dto.getToCurrency()));
        Float exchangeRate = dto.getExchangeRate()==null ? null : Float.valueOf(dto.getExchangeRate());
        CurrencyRates currencyRates= currencyRatesService.generateExchangeRate(
                fromCurrency,toCurrency,invoice.getCurrencyRates(), exchangeRate);

        invoiceService.updateInvoice(
                invoice.getId(), dto, portfolio, serviceProvided, bankAccount, vat, custodyCharge,currencyRates,status);
    }



}
