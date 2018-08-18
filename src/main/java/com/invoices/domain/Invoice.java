package com.invoices.domain;

import com.invoices.enumerations.InvoiceFrequency;
import com.invoices.enumerations.InvoicePeriod;
import com.invoices.enumerations.InvoiceType;
import com.invoices.enumerations.IsApplicable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents a record in the invoices table. (An Invoice)
 * @author psoutzis
 */
@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type")
    private InvoiceType invoiceType;

    @Column(name = "invoice_number", unique = true)
    private String invoiceNumber;

    @Enumerated(EnumType.STRING)
    private InvoiceFrequency frequency;

    @Enumerated(EnumType.STRING)
    private InvoicePeriod period;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_issued")
    private Date invoiceDate;

    @Column(name = "year_issued")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "reverse_charge")
    private IsApplicable reverseCharge;

    @Enumerated(EnumType.STRING)
    @Column(name = "vat_exempt")
    private IsApplicable vatExempt;

    //FOREIGN KEY MAPPING
    //Maps service to charge for with
    //corresponding service from Services table
    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceProvided serviceProvided;

    @ManyToOne
    @JoinColumn(name = "bank_acc_id")
    private BankAccount bankAccount;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "currency_rate_id")
    private CurrencyRates currencyRates;

    @ManyToOne
    @JoinColumn(name = "vat_id")
    private Vat vat;

    @ManyToOne
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "custody_charge_id")
    private CustodyCharge custodyCharge;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private InvoiceStatus invoiceStatus;
}
