package com.invoices.enumerations;

/**
 * Enum class that holds all types that an invoice can have.
 * This adds scalability and could be updated in the future if more
 * invoice types are needed.
 * @author psoutzis
 */
public enum InvoiceType {
    DRAFT,
    REAL,
    OTHER
}
