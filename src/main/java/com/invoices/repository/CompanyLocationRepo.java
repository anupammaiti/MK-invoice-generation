package com.invoices.repository;

import com.invoices.domain.CompanyLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyLocationRepo extends
        JpaRepository<CompanyLocation, Long> {
}
