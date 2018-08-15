package com.invoices.repository;

import com.invoices.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Invoice Entity
 * @author psoutzis
 */
@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    Invoice getInvoiceById(Long id);
}
