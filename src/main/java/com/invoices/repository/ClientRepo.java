package com.invoices.repository;

import com.invoices.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author psoutzis
 * The repository for the Client Entity
 */
@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
}
