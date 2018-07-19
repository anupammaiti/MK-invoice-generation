package com.invoices.controller;

import com.invoices.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class HomeController {

    @Autowired private InvoiceService invoiceService;

    @GetMapping("/")
    public String init(){

        return "initial.html";
    }

    @GetMapping("/select/{action}")
    public String userSelection(Model model,
                                @PathVariable("action") String action,
                                @RequestParam("buttonId") String buttonId,
                                @RequestParam("script") String script){
        model.addAttribute("invoices", invoiceService.getInvoices());
        model.addAttribute("action", action);
        model.addAttribute("buttonValue", StringUtils.capitalize(action));
        model.addAttribute("buttonId", buttonId);
        model.addAttribute("script", script);

        return "select-invoice";
    }

    @GetMapping("/success/{result}")
    public String successMessage(Model model,
                                 @PathVariable("result") String result){
        model.addAttribute("result", result);

        return "success";
    }
}
