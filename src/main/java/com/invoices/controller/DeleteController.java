package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller class responsible for deleting an invoice from that database.
 * @author Petros Soutzis
 */
@Controller
public class DeleteController {
    @Autowired InvoiceService invoiceService;

    /**
     * Method will delete the invoice that it received as a JSON
     * @param invoice is the Invoice-type object to delete from the database
     */
    @PostMapping("/find/delete")
    @ResponseBody
    public void deleteInvoice(@RequestBody Invoice invoice){

        invoiceService.deleteRecord(invoice.getId());
    }
}
