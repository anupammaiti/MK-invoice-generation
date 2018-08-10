package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author psoutzis
 * This controller will be responsible of using Invoice Template to
 * generate invoices into PDF format
 */
@Controller
public class PdfController {
    @Autowired InvoiceService invoiceService;
    @Autowired PdfService pdfService;
    private final String PDF_PARENT = "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/pdf";
    private final boolean STORE_PDF_ON_SERVER = false;

    @PostMapping(value = "/generate-pdf")
    @ResponseBody
    public void generatePdf(@RequestParam ("id") String id, HttpServletResponse response) {
        Invoice invoice = invoiceService.getInvoiceById(Long.valueOf(id));

        //Generate a path, based on the invoices number. (Or use generateCustomPdfPath() method to assign custom name).
        String path = pdfService.generatePdfPath(PDF_PARENT, invoice.getInvoiceNumber());

        pdfService.createPdf(invoice, path);
        pdfService.sendBytesToBrowser(response, path, STORE_PDF_ON_SERVER);
    }

}
