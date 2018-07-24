package com.invoices.dto;

import lombok.Getter;

@Getter
public class UpdateInvoiceDTO {
    private String invoiceDate;
    private String invoiceType;
    private String vatExempt;
    private String reverseCharge;
    private String vatApplicable;
    private String invoiceNumber;
    private String frequency;
    private String period;
    private String id;
    private String portfolio;
}
