package com.invoices.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

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
