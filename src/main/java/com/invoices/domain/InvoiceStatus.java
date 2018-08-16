package com.invoices.domain;

import com.invoices.enumerations.IsApplicable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * An Entity that represents the status of the invoice (SENT/NOT SENT, PAID/NOT PAID)
 * @author psoutzis
 */
@Entity
@Table(name = "invoice_status")
@Getter @Setter @NoArgsConstructor
public class InvoiceStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Long statusId;

    @Enumerated(EnumType.STRING)
    private IsApplicable sent;

    @Enumerated(EnumType.STRING)
    private IsApplicable paid;
}
