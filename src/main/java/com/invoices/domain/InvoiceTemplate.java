package com.invoices.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author psoutzis
 * This class will hold all the fields to be included in the PDF document.
 */
@Service    //not a service. Just temporary
@Getter
@Setter
@NoArgsConstructor
public class InvoiceTemplate {

    private String companyName;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private String vatNumber;
    private Date invoiceDate;
    private String invoiceNumber;
    private String description;
    private Float chargeExcludingVat;
    private Float chargeIncludingVat;
    private Float vatCharge;
    private Float vatRate;
    private Float currencyRate;

    public InvoiceTemplate(String companyName, String address, String postcode, String city,
                           String country, String vatNumber, Date invoiceDate, String invoiceNumber,
                           Float chargeExcludingVat, Float chargeIncludingVat, Float vatCharge,
                           Float vatRate, Float currencyRate)
    {
        this.companyName = companyName;
        this.address = address;
        this.postcode = postcode;
        this.city = city;
        this.country = country;
        this.vatNumber = vatNumber;
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.chargeExcludingVat = chargeExcludingVat;
        this.chargeIncludingVat = chargeIncludingVat;
        this.vatCharge = vatCharge;
        this.vatRate = vatRate;
        this.currencyRate = currencyRate;
    }

    public void setDescription(String service, String frequency,
                               String period, String year) {
        this.description = "Fee for "+ service +" services provided, " +
                "as per agreement for "+ frequency + period +" "+ year;
    }

    public void setDescription(String customMessage){

        this.description = customMessage;
    }
}
