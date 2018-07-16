package com.invoices.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Long id;

    @Column(name = "client_name")
    private String clientName;

    //maps client to their company(s)
    @OneToMany
    @JoinColumn(name = "company_id")
    private List <ClientCompanyInfo> clientCompanyInfoList;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List <Portfolio> portfolioList;
}
