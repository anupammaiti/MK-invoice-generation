package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * This Entity holds the rates of currencies, for the date that an invoice was issued
 * E.g: An invoice is issued on the 10th of July 2018, 10:00 a.m. The rates needed
 * by the application will be fetched, for the given date & time and will be stored to
 * the database. This is the parent table, so an invoice record will have a foreign key
 * mapping it to the appropriate currency rate record.
 */
@Entity
@Table(name = "currency_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rate_id")
    private Long currencyRateId;

    @Column(name="EUR/USD")
    private Float euroToUsd;

    @Column(name="EUR/JPY")
    private Float euroToJpy;

    @Column(name="EUR/GBP")
    private Float euroToGbp;

    @Column(name="GBP/USD")
    private Float GbpToUsd;

    @Column(name="JPY/USD")
    private Float JpyToUsd;

    @OneToOne(mappedBy = "currencyRates", cascade = CascadeType.ALL)
    private Invoice invoice;
}
