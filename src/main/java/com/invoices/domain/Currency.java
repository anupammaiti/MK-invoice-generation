package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author psoutzis
 * The Currency entity represents the currency that an invoice will be issued in.
 * E.g: an invoice could be issued in United States Dollars or Euros. (USD, EUR)
 */
@Table(name = "currencies")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "currency_code", nullable = false)
    private String currencyCode;

    /*@OneToMany(mappedBy = "currency")
    private List<Invoice> invoiceList;

    @OneToMany(mappedBy = "fromCurrency")
    private List<CurrencyRates> exchangeRateFromCurrency;

    @OneToMany(mappedBy = "toCurrency")
    private List<CurrencyRates> exchangeRateToCurrency;*/
}
