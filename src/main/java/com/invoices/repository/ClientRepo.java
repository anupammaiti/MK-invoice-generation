package com.invoices.repository;

import com.invoices.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Client Entity
 * @author psoutzis
 */
@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
}
