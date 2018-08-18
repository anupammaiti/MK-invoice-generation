package com.invoices.repository;

import com.invoices.domain.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the ServiceProvided Entity
 * @author psoutzis
 */
@Repository
public interface ServiceProvidedRepo extends JpaRepository<ServiceProvided, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    ServiceProvided findServiceProvidedById(Long id);
}
