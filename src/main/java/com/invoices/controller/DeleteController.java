package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DeleteController {
    @Autowired InvoiceService invoiceService;

    @PostMapping("/find/delete")
    @ResponseBody
    public void deleteInvoice(@RequestBody Invoice invoice){

        invoiceService.deleteRecord(invoice.getId());
    }
}