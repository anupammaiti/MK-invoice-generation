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
    private String vatRate;
    private Long bankAccount;
    private Long portfolio;
    private Long fromCurrency;
    private Long serviceProvided;
    private Long toCurrency;
    private Float baseCharge;
    private Integer year;

}
