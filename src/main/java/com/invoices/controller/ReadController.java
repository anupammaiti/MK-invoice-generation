package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReadController {
    @Autowired InvoiceService invoiceService;
    private Invoice invoice;


    @PostMapping("/find/read")
    @ResponseBody
    public void findInvoice(@RequestBody Invoice invoice){

        this.invoice = invoiceService.getInvoiceById(invoice.getId());
    }

    @GetMapping("/viewInvoice")
    public String viewInvoice(Model model){
        model.addAttribute("invoice", invoice);
        model.addAttribute("company", invoice.getPortfolio().getClientCompanyInfo());
        this.invoice = null;
        return "read/viewInvoice";
    }

}
