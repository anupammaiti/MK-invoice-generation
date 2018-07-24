package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.dto.InvoiceDTO;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.enumerations.InvoiceFrequency;
import com.invoices.enumerations.InvoicePeriod;
import com.invoices.enumerations.InvoiceType;
import com.invoices.enumerations.IsApplicable;
import com.invoices.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UpdateController {

    @Autowired InvoiceService invoiceService;
    @Autowired PortfolioService portfolioService;
    @Autowired ServiceProvidedService serviceProvidedService;
    @Autowired CurrencyService currencyService;
    @Autowired BankAccountService bankAccountService;
    @Autowired VatService vatService;

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
        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());

        return "update-attributes";
    }

    @PostMapping("/find/update/execute")
    @ResponseBody
    public void executeUpdate(@RequestBody UpdateInvoiceDTO updateInvoiceDTO){
        Invoice invoice = invoiceService.updateInvoiceAttributes(updateInvoiceDTO);
        invoiceService.save(invoice);
    }



}
