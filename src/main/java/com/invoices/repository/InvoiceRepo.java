package com.invoices.repository;

import com.invoices.domain.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    /**
     * @param fromDate The minimum date an invoice can have
     * @param toDate The maximum date an invoice can have
     * @return List of invoices, that have dates which are within the boundaries passed as parameters.
     */
    @Query("SELECT i FROM Invoice i WHERE i.invoiceDate >= ?1 AND i.invoiceDate <= ?2")
    List<Invoice> getInvoicesByDateBetween(Date fromDate, Date toDate);
}
