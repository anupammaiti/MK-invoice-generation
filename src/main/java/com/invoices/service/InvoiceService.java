package com.invoices.service;

import com.invoices.domain.Invoice;
import com.invoices.repository.InvoiceRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepo invoiceRepo;

    @Getter@Setter
    private Invoice invoice;

    public Invoice save(Invoice invoice){

        return invoiceRepo.save(invoice);
    }
}
