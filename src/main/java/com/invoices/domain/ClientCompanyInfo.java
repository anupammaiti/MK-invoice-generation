package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author psoutzis
 * This entity holds information about a client's company(s). Location, name, etc..
 */

@Entity
@Table(name = "client_companies_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientCompanyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(name = "company_name")
    private String name;

    @Column(name = "vat_number")
    private String vatNumber;
    private String address;
    private String city;
    private String postcode;

    //mapping to client that owns company
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(mappedBy = "clientCompanyInfo", cascade = CascadeType.ALL)
    private Portfolio portfolio;

    //mapping to company_location
    @ManyToOne
    @JoinColumn(name = "location_id")
    private CompanyLocation companyLocation;
}
