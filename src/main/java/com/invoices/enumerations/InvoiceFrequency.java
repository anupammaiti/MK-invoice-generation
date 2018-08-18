package com.invoices.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This enumeration represents the 'Frequency' options of an invoice
 * @author psoutzis
 */
@AllArgsConstructor
public enum InvoiceFrequency {
    MONTHLY("MONTHLY"),
    QUARTERLY("QUARTERLY"),
    SEMI_ANNUALLY("SEMI ANNUALLY"),
    ANNUALLY("ANNUALLY"),
    OTHER("OTHER");

    @Getter
    private String description;
}
