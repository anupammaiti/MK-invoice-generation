package com.invoices.controller;

import com.invoices.domain.Invoice;
import com.invoices.service.InvoiceService;
import com.invoices.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author psoutzis
 * This controller is responsible for generating a PDF representing the requested invoice.
 * <u>NOTE</u>: If you need to store the pdf files locally, change the MACRO variable 'STORE_PDF_ON_SERVER'
 * to <b>True</b>.
 */
@Controller
public class PdfController {
    @Autowired InvoiceService invoiceService;
    @Autowired PdfService pdfService;
    private final String PDF_PARENT = "C:/Users/psoutzis/Desktop/myFolder/projects/invoices/src/main/resources/pdf";
    private final boolean STORE_PDF_ON_SERVER = false;

    /**
     * Method will send the bytes of the newly created .pdf file to the browser, making it available for download.
     * It will delete the file from local storage afterwards.
     * @param id Is the primary key of the invoice to generate a PDF from.
     * @param response Is the HTTP response that will write the pdf bytes to the browser
     */
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
