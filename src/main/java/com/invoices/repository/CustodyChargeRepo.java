package com.invoices.repository;

import com.invoices.domain.CustodyCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author psoutzis
 * The repository for the Custody charge Entity
 */
@Repository
public interface CustodyChargeRepo extends
        JpaRepository<CustodyCharge, Long> {
}
