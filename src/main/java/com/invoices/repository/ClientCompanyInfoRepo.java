package com.invoices.repository;

import com.invoices.domain.ClientCompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * The repository for the ClientCompanyInfo Entity
 * @author psoutzis
 */
@Repository
public interface ClientCompanyInfoRepo extends JpaRepository<ClientCompanyInfo, Long> {

    /**
     * @param id The primary key of the record to return
     * @return The record whose primary key is equal to the method argument
     */
    ClientCompanyInfo findClientCompanyInfoById(Long id);
}
