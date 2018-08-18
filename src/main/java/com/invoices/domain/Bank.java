package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * This Entity represents banks e.g: Bank of Cyprus, Barclays, etc..
 * MeritKapital accounts always have a corresponding bank. There can be several
 * accounts opened in the same bank
 * @author psoutzis
 */

@Entity
@Table(name = "banks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id")
    private Long id;

    @Column(name = "bank_name")
    private String name;

    @OneToMany(mappedBy = "bank")
    private List<BankAccount> bankAccountList;
}
