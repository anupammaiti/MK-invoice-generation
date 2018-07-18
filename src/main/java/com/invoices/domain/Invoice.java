package com.invoices.domain;

import lombok.*;
import javax.persistence.*;
import java.util.Date;

/**
 * @author psoutzis
 * This class represents a record in the invoices table.
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
    private String frequency;
    private String period;

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

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "currency_rate_id")
    private CurrencyRates currencyRates;

    @ManyToOne
    @JoinColumn(name = "vat_id")
    private Vat vat;

    @ManyToOne//CAN WE ISSUE MORE THAN 1 INVOICES TO A PORTFOLIO?
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "custody_charge_id")
    private CustodyCharge custodyCharge;
}
