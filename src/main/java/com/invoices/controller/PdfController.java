package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.service.PdfService;
import com.itextpdf.layout.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author psoutzis
 * This controller will be responsible of using Invoice Template to
 * generate invoices into PDF format
 */
@Controller
//TODO CREATE FUNCTION TO AUTOMATICALLY VIEW PDF IN BROWSER
public class PdfController {
    @Autowired InvoiceService invoiceService;
    @Autowired PdfService pdfService;
    private final String PDF_PARENT = "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/pdf";

    @GetMapping(value = "/generate-pdf")
    @ResponseBody
    public void generatePdf() {
        Invoice invoice = invoiceService.getInvoiceById(48L);
        String filename = pdfService.generatePdfPath(PDF_PARENT,invoice.getInvoiceNumber());
        //String filename = pdfService.generateCustomPath(PDF_PARENT+"/","pedrulo" ,".pdf" );

        Document pdf = pdfService.createPdf(invoice, filename);
    }

}
