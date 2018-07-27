package com.invoices.dto;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.domain.CompanyLocation;
import com.invoices.domain.Portfolio;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UpdateInvoiceDTO {
    /*private String invoiceDate;
    private String vatExempt;
    private String reverseCharge;
    private String vatApplicable;
    private String invoiceNumber;
    private String frequency;
    private String period;
    */
    private String companyAddress;
    private String companyCity;
    private String companyCountry;
    private String companyName;
    private String companyPostcode;
    private String companyVatNumber;
    private String id;
    private String portfolio;
    private String invoiceType;

    @Setter private ClientCompanyInfo clientCompanyInfo;
    @Setter private Portfolio updatePortfolio;

}
