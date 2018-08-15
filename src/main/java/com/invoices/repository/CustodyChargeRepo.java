package com.invoices.repository;

import com.invoices.domain.CustodyCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Custody charge Entity
 * @author psoutzis
 */
@Repository
public interface CustodyChargeRepo extends JpaRepository<CustodyCharge, Long> {
}
