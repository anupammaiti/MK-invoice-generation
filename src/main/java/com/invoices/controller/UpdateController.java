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

/**
 * @author psoutzis, email: soutzis.petros@gmail.com
 * This class is a controller responsible for managing the HTTP requests about invoice updates.
 * Updates made to an invoice, will be automatically propagated all the respective tables in the database,
 * by using the Hibernate framework.
 */

//TODO when user enters manual vat rate, count number of characters and add appropriate precision to findByVatRate method
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

    /**
     * @param id the id of the invoice to update.
     * @param model is the model that will hold data to be displayed by the view
     * @return the page where the user will make their invoice-changes at
     * WARNING: Html and JavaScript code depends on the NAMING of MODEL VARIABLES. Renaming could lead to bugs.
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

        return "update-attributes";
    }

    /**
     * Method that listens for the HTTP request to apply the selected updates
     * @param updateInvoiceDTO a Data Transfer Object (DTO) to hold all data
     * relevant to the incoming update request.
     */
    @PostMapping("/find/update/execute")
    @ResponseBody
    public void executeUpdate(@RequestBody UpdateInvoiceDTO updateInvoiceDTO){
        Portfolio portfolio = portfolioService.getRecord(Long.valueOf(updateInvoiceDTO.getPortfolio()));
        Client client = portfolio.getClient();
        CompanyLocation location = companyLocationService.getRecordAndSave(updateInvoiceDTO.getCompanyCountry());
        ClientCompanyInfo tempCompany = clientCompanyInfoService.getRecord(updateInvoiceDTO.getCompanyId());
        tempCompany.setClient(client);
        tempCompany.setCompanyLocation(location);
        ClientCompanyInfo company = clientCompanyInfoService
                .detectChangesAndApply(updateInvoiceDTO,tempCompany);

        portfolio.setClientCompanyInfo(company);
        Invoice invoice = invoiceService.updateInvoice(updateInvoiceDTO, portfolio);
        invoiceService.save(invoice);
    }



}
