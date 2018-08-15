package com.invoices.dto;

import lombok.Getter;

/**
 * A Data Transfer Object (DTO), that will be used to temporarily hold the data
 * relevant to an invoice update.
 * @author psoutzis
 */
@Getter
public class UpdateInvoiceDTO {


    /* primary/foreign keys */
    private Long invoiceId;
    private Long companyId;
    private String portfolio;
    private String fromCurrency;
    private String toCurrency;
    private String serviceProvided;
    private String bankAccount;
    /* ******************** */

    /* date of invoice */
    private String invoiceDate;
    /* ************** */

    /* enumerations */
    private String vatExempt;
    private String reverseCharge;
    private String vatApplicable;
    private String invoiceType;
    private String frequency;
    private String period;
    /* *********** */

    /* company info */
    private String companyAddress;
    private String companyCity;
    private String companyCountry;
    private String companyName;
    private String companyPostcode;
    private String companyVatNumber;
    /* *********** */

    /* Other */
    private Float vatRate;
    private String invoiceNumber;
    private String year;
    private String custodyCharge;
    private String exchangeRate;
    /* ***** */
}
