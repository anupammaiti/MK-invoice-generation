package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Example;

import javax.persistence.*;
import java.util.List;

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

    //Defines the list of invoices that apply
    //the given vatRate
    @OneToMany(mappedBy = "vat", cascade = CascadeType.ALL)
    private List<Invoice> invoiceList;
}
