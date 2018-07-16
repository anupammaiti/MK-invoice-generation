package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "currency_rate")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rate_id")
    private Long currencyRateId;

    @NotNull
    private float rate;

    @OneToMany(mappedBy = "currencyRate", cascade = CascadeType.ALL)
    private List<Invoice> invoiceList;
}
