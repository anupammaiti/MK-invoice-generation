package com.invoices.repository;

import com.invoices.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author psoutzis
 * The repository for the Invoice Entity
 */
@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, Long> {
    Invoice getInvoiceById(Long id);
}
