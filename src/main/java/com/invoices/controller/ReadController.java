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

/**
 * This class is the controller responsible for retrieving all relevant data to the invoice
 * and adding them to the View, where they will be rendered to an appropriate user-friendly view,
 * similar to the previous style of the company's invoices.
 * @author Petros Soutzis
 */
@Controller
public class ReadController {

    @Autowired InvoiceService invoiceService;

    /**
     * @param invoiceIdString The primary key of the invoice to view (received as a String).
     * @param model The Model component that will render all needed values to the view.
     * @return The html file with the loaded information, to represent an Invoice-type object (invoice).
     */
    @PostMapping(value = "/find/read")
    public String readPdfElementsInBrowser(@RequestParam("id") String invoiceIdString, Model model){
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
        Float vatCharge = ExchangeRateProviderHandler
                .convertToCurrency(charge.getVatCharge(),xRate.getExchangeRate());
        Float total = ExchangeRateProviderHandler
                .convertToCurrency(charge.getChargeIncludingVat(),xRate.getExchangeRate());
        //creating a new CustodyCharge object for converted currency
        CustodyCharge convertedCharge = new CustodyCharge(null,vatCharge,total,base);

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

    /**
     * Method will filter the results, to display only invoices that their date is between the dates specified by user.
     * @param fromDate Minimum date to display
     * @param toDate Maximum date to display
     * @param model Model component to add data to the view
     * @return The invoices that have a date, within the boundaries specified by the user
     */
    @GetMapping(value = "/find/filter")
    public String filterResultsByDate(@RequestParam("fromDate") String fromDate,
                                      @RequestParam("toDate") String toDate,
                                      Model model){
        model.addAttribute("invoices", invoiceService.getInvoicesByDateBetween(fromDate, toDate));

        return "select-invoice-view-collective-data.html";
    }

}
