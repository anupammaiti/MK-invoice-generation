package com.invoices.controller;

import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Petros Soutzis
 * This class is the controller responsible for handling requests for
 * actions like creating or deleting an invoice. It will map to the
 * appropriate controller method.
 */
@Controller
public class HomeController {
    //TODO ADD VALIDATION TO ALL CONTROLLER METHODS; maybe add security?
    //TODO ADD FUNCTIONALITY SO USER CAN INSERT INVOICE DESCRIPTION?
    @Autowired private InvoiceService invoiceService;

    @GetMapping("/")
    public String init(){

        return "initial.html";
    }

    @GetMapping("/select/{action}")
    public String userSelection(Model model, @PathVariable("action") String action){
        model.addAttribute("invoices", invoiceService.getInvoices());
        model.addAttribute("action", action);

        return "select-invoice-"+action+".html";
    }

    @GetMapping("/success/{result}")
    public String successMessage(Model model, @PathVariable("result") String result){
        model.addAttribute("result", result);

        return "success";
    }
}
