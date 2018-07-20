package com.invoices.domain;

import com.invoices.enumerations.IsApplicable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * This entity represents a VAT rate. e.g: 0.19 or 0.19078
 */
@Entity
@Table(name = "vat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vat_id")
    private Long vatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "vat_applicable")
    private IsApplicable isApplicable;

    @Column(name = "vat_rate")
    private Float vatRate;

    /*@OneToMany(mappedBy = "vat")
    private List<Invoice> invoiceList;*/
}
