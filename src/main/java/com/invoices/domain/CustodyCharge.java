package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Table(name = "custody_charges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustodyCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "custody_charge_vat")
    private float vatCharge;

    @Column(name = "custody_charge_incl_vat")
    private float chargeIncludingVat;

    @Column(name = "custody_charge_excl_vat")
    private float chargeExcludingVat;

    //Child has to set parent
    @OneToOne()
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
