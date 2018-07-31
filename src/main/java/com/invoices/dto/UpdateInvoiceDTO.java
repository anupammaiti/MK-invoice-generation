package com.invoices.dto;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.domain.CompanyLocation;
import com.invoices.domain.Portfolio;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * @author psoutzis
 * A Data Transfer Object (DTO), that will be used to temporarily hold the data
 * relevant to an invoice update.
 */
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
    private Long invoiceId;
    private Long companyId;
    private String companyAddress;
    private String companyCity;
    private String companyCountry;
    private String companyName;
    private String companyPostcode;
    private String companyVatNumber;
    private String portfolio;
    private String invoiceType;

}
