package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/updateInvoice")
    public Invoice updateInvoice(Invoice invoice) {
        invoiceService.save(invoice);
        return invoice;
    }

    @GetMapping("/invoice/{invoiceId}")
    public Invoice getInvoice(@PathVariable(name = "invoiceId") String invoiceId) {
        Long id = Long.valueOf(invoiceId);
        return invoiceService.getInvoiceById(id);
    }
}
