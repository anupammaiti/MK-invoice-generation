package com.invoices.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author psoutzis
 * This entity represents the country that a given company is located at.
 */

@Entity
@Table(name = "company_location")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;

    @NotNull
    private String country;
}
