package com.invoices.repository;

import com.invoices.domain.CompanyLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the CompanyLocation Entity
 * @author psoutzis
 */
@Repository
public interface CompanyLocationRepo extends JpaRepository<CompanyLocation, Long> {

    /**
     *
     * @param country The key to search a record for
     * @return The record whose 'country' attribute value matches the argument
     */
    CompanyLocation findCompanyLocationByCountry(String country);
}
