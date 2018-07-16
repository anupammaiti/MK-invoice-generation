package com.invoices.repository;

import com.invoices.domain.Vat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * vat table in invoices database would be more effective if
 * vat applicable attribute was moved to invoices
 */
@Repository
public interface VatRepo extends JpaRepository<Vat, Long>,QueryByExampleExecutor<Vat> {

    /*
    @Query("select v from Vat v where v.vatRate BETWEEN ?1 and ?2")
    List<Vat> findByVatRate(Float vatRate, Float accuracy);
    */

    Vat findVatByVatId(Long id);
}
