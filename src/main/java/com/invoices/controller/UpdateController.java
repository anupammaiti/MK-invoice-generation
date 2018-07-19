package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.domain.Portfolio;
import com.invoices.service.InvoiceService;
import com.invoices.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UpdateController {

    @Autowired InvoiceService invoiceService;
    @Autowired PortfolioService portfolioService;

    private Invoice invoice;

    @PostMapping("/find/update")
    @ResponseBody
    public void findToUpdate(@RequestBody Invoice invoice){
        Long invoiceId = invoice.getId();
        this.invoice = invoiceService.getInvoiceById(invoiceId);
        /*invoice.setPeriod("Santa Claus");//JUST A TEST
        Portfolio newPortfolio = portfolioService.getRecord(4L);//JUST A TEST
        invoice.setPortfolio(newPortfolio);
        invoiceService.save(invoice);*/

        //return "forward:/invoice/update";
    }

    @GetMapping("/invoice/update")
    public String updateInvoice (Model model){
        model.addAttribute("invoice", this.invoice);
        model.addAttribute("company", this.invoice.getPortfolio().getClientCompanyInfo());
        return "update/update-attributes";
    }


}
