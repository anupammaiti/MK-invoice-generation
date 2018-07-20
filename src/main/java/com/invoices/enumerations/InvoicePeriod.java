package com.invoices.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum InvoicePeriod {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    JAN("JANUARY"),
    FEB("FEBRUARY"),
    MAR("MARCH"),
    APR("APRIL"),
    MAY("MAY"),
    JUN("JUNE"),
    JUL("JULY"),
    AUG("AUGUST"),
    SEP("SEPTEMBER"),
    OCT("OCTOBER"),
    NOV("NOVEMBER"),
    DEC("DECEMBER");

    @Getter
    private String description;
}
