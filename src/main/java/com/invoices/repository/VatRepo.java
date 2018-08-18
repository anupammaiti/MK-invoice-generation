package com.invoices.repository;

import com.invoices.domain.Vat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * The repository for the Vat Entity
 * @author psoutzis
 */
@Repository
public interface VatRepo extends JpaRepository<Vat, Long>,QueryByExampleExecutor<Vat> {

    /**
     * @param negativeAccuracy Result can not be less than this
     * @param positiveAccuracy Result can not be more than this
     * @return a Vat record, whose 'rate' matches a value that is between negativeAccuracy and positiveAccuracy
     */
    @Query("select v from Vat v where v.vatRate BETWEEN ?1 and ?2")
    Vat findByAccurateVatRate(Float negativeAccuracy, Float positiveAccuracy);

    /**
     * @param rate The vat rate of the record to fetch.
     * @return The record whose attribute <b>"vat_rate"</b> has a value equal to the argument.
     */
    Vat findVatByVatRate(Float rate);

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    Vat findVatByVatId(Long id);
}
