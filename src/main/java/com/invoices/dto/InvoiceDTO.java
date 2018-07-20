package com.invoices.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class InvoiceDTO {
    private Date invoiceDate;
    private String invoiceType;
    private String vatExempt;
    private String reverseCharge;
    private String vatApplicable;
    private String invoiceNumber;
    private String frequency;
    private String period;
    private long bankAccount;
    private long portfolio;
    private long fromCurrency;
    private long serviceProvided;
    private long vat;
    private long toCurrency;
    private float baseCharge;
    private int year;

}
