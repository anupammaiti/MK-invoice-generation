package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UpdateController {

    @Autowired InvoiceService invoiceService;
    //@Autowired PortfolioService portfolioService;

    @PostMapping(value = "/find/update")
    public String findToUpdate(@RequestParam ("id") String id, Model model){
        Long invoiceId = Long.valueOf(id);
        Invoice invoice = invoiceService.getInvoiceById(invoiceId);
        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());

        return "update-attributes";
    }

    /*@GetMapping("/getinvoice/{id}")
    @ResponseBody
    public Invoice updateInvoice (@PathVariable("id") String InvoiceId){
        Long id = Long.parseLong(InvoiceId);

        return invoiceService.getInvoiceById(id);
    }*/


}
