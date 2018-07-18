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
    String[] actions = {"CREATE", "READ", "UPDATE", "DELETE"};
    @GetMapping("/selectToUpdate")
    public String selectInvoiceToDelete(Model model){
        model.addAttribute("invoices", invoiceService.getInvoices());
        model.addAttribute("action", actions[2]);

        return "update/selectInvoiceToUpdate";
    }

    @PostMapping("/findToUpdate")
    @ResponseBody
    public void findToUpdate(@RequestBody Invoice invoice){
        invoice = invoiceService.getInvoiceById(invoice.getId());   ///////////////
        invoice.setPeriod("Octoberisimo");                          //JUST A TEST//
        Portfolio newPortfolio = portfolioService.getRecord(4L);//           //
        invoice.setPortfolio(newPortfolio);                        ///////////////
        invoiceService.save(invoice);

        this.invoice = invoice;
    }

    @GetMapping("/updateInvoice")
    public String updateInvoice (Model model){
        model.addAttribute("invoice", this.invoice);
        model.addAttribute("company", this.invoice.getPortfolio().getClientCompanyInfo());
        return "update/updateInvoiceAttributes";
    }


}
