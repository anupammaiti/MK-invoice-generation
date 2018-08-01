package com.invoices.controller;

import com.invoices.domain.CurrencyRates;
import com.invoices.domain.CustodyCharge;
import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.utils.ExchangeRateProviderHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class ReadController {

    @Autowired InvoiceService invoiceService;

    @PostMapping(value = "/find/read")
    public String findInvoice(@RequestParam("id") String invoiceIdString, Model model){
        Long invoiceId = Long.valueOf(invoiceIdString);
        final Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        CurrencyRates xRate = invoice.getCurrencyRates();
        CustodyCharge charge = invoice.getCustodyCharge();

        //variables needed, to be displayed in the view
        String currencySymbolFrom = ExchangeRateProviderHandler
                .getCurrencySymbol(xRate.getFromCurrency().getCurrencyCode());
        String currencySymbolTo = ExchangeRateProviderHandler
                .getCurrencySymbol(xRate.getToCurrency().getCurrencyCode());
        Float base = ExchangeRateProviderHandler
                .convertToCurrency(charge.getChargeExcludingVat(),xRate.getExchangeRate());
        Float vatValue = ExchangeRateProviderHandler
                .convertToCurrency(charge.getVatCharge(),xRate.getExchangeRate());
        Float target = ExchangeRateProviderHandler
                .convertToCurrency(charge.getChargeIncludingVat(),xRate.getExchangeRate());
        //creating a new CustodyCharge object for converted currency
        CustodyCharge convertedCharge = new CustodyCharge(base,vatValue,target);

        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());
        model.addAttribute("bank", invoice.getBankAccount());
        model.addAttribute("xRate", xRate);
        model.addAttribute("currencySymbolFrom", currencySymbolFrom);
        model.addAttribute("currencySymbolTo", currencySymbolTo);
        model.addAttribute("charge", charge);
        model.addAttribute("convertedCharge", convertedCharge);

        return "read/read-invoice";
    }



    /*
    @PostMapping(value = "/find/read")
    public String findInvoice(@RequestParam("id") String invoiceIdString, RedirectAttributes flash){
        Long invoiceId = Long.valueOf(invoiceIdString);
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        flash.addFlashAttribute("invoice", invoice);

        return "redirect:/find/read";
    }

    @GetMapping(value = "/find/read")
    public String viewInvoice(@ModelAttribute("invoice") Invoice attr, Model model){
        final Invoice invoice = attr;
        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());

        return "read/read-invoice";
    }
     */
}
