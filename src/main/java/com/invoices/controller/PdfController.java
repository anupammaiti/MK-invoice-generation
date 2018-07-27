package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.utils.PdfCreator;
import com.itextpdf.layout.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author psoutzis
 * This controller will be responsible of using Invoice Template to
 * generate invoices into PDF format
 */
@Controller
public class PdfController {
    @Autowired
    InvoiceService invoiceService;

    @GetMapping(value = "/pdf-generation", produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseBody
    public void generatePdf() throws IOException {
        String generatedInvoicePath = "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/PDFs";
        Invoice customInvoice = invoiceService.getInvoiceById(49L);

        Document pdf = PdfCreator.createPdf(customInvoice, generatedInvoicePath);
    }

}
