package com.invoices.repository;

import com.invoices.domain.ClientCompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * @author psoutzis
 * The repository for the ClientCompanyInfo Entity
 */
@Repository
public interface ClientCompanyInfoRepo extends
        JpaRepository<ClientCompanyInfo, Long> {
}
