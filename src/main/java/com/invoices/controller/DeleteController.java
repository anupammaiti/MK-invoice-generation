package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class DeleteController {
    @Autowired InvoiceService invoiceService;

    @GetMapping("/selectToDelete")
    public String selectInvoiceToDelete(Model model){
        model.addAttribute("invoices", invoiceService.getInvoices());

        return "delete/selectInvoiceToDelete";
    }

    @PostMapping("/findAndDelete")
    @ResponseBody
    public void findAndDelete(@RequestBody Invoice invoice){

        invoiceService.deleteRecord(invoice.getId());
    }

    @GetMapping("/deleteInvoice")
    public String successfulDelete(){

        return "delete/deleteSuccess";
    }
}
