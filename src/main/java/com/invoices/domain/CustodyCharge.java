package com.invoices.domain;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;

/**
 * @author psoutzis
 * This entity represents the fee that will be paid to a MeritKapital bank account.
 * custody_charge_excl_vat is the original fee to be paid to MK.
 *
 * i.e: custody_charge_excl_vat = 1000, then (assuming vat rate = 19%)
 * custody_charge_vat = 190,
 * custody_charge_incl_vat = 1190
 *
 * An invoice will always have a mapping in the custody_charges table.
 */
@Entity
@Table(name = "custody_charges")
@Getter
@Setter
@NoArgsConstructor
public class CustodyCharge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custody_charge_id")
    private Long custodyChargeId;

    @Column(name = "custody_charge_vat")
    private float vatCharge;

    @Column(name = "custody_charge_incl_vat")
    private float chargeIncludingVat;

    @Column(name = "custody_charge_excl_vat")
    private float chargeExcludingVat;

    public CustodyCharge(float chargeExcludingVat, float vatCharge, float chargeIncludingVat) {
        this.vatCharge = vatCharge;
        this.chargeIncludingVat = chargeIncludingVat;
        this.chargeExcludingVat = chargeExcludingVat;
    }
}
