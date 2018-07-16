package com.invoices.repository;

import com.invoices.domain.ServiceProvided;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author psoutzis
 * The repository for the ServiceProvided Entity
 */
@Repository
public interface ServiceProvidedRepo extends JpaRepository<ServiceProvided, Long> {

    ServiceProvided findServiceProvidedByServiceName(String serviceName);
    ServiceProvided findServiceProvidedById(Long id);
}
