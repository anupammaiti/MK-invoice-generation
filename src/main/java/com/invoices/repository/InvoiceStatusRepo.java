package com.invoices.repository;

import com.invoices.domain.InvoiceStatus;
import com.invoices.enumerations.IsApplicable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the InvoiceStatus Entity
 * @author psoutzis
 */
@Repository
public interface InvoiceStatusRepo extends JpaRepository<InvoiceStatus, Long> {

    InvoiceStatus getInvoiceStatusBySentAndPaid(IsApplicable sent, IsApplicable paid);
}
