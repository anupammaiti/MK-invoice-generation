package com.invoices.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
