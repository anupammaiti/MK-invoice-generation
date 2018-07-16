package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "mk_bank_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Long id;

    @Column(name="account_name")
    private String name;

    @Column(name="swift_code")
    private String swiftCode;

    @Column(name="euro_acc_num")
    private String euroAccNum;

    @Column(name="euro_iban")
    private String usdAccNum;

    @Column(name="usd_acc_num")
    private String euroIban;

    @Column(name="usd_iban")
    private String usdIban;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

    @OneToMany(mappedBy = "bankAccount")
    private List<Invoice> invoiceList;
}
