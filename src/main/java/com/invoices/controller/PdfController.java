package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.service.PdfService;
import com.itextpdf.layout.Document;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired InvoiceService invoiceService;
    @Autowired PdfService pdfService;
    private final String PDF_PARENT = "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/PDFs";

    @GetMapping(value = "/pdf-generation")
    @ResponseBody
    public void generatePdf() throws IOException {
        Invoice customInvoice = invoiceService.getInvoiceById(48L);

        Document pdf = pdfService.createPdf(customInvoice, PDF_PARENT);
    }

}
