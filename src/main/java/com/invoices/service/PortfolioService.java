package com.invoices.service;

import com.invoices.domain.Portfolio;
import com.invoices.repository.PortfolioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated as a service.
 * It is the service bean for the Portfolio entity
 * @author psoutzis
 */
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepo portfolioRepo;

    /**
     * @param id The primary key of the record to return
     * @return The Portfolio object whose id is equal to the method argument
     */
    public Portfolio getRecord(Long id){

        return portfolioRepo.getPortfolioById(id);
    }

    /**
     * @return A collection of all the Portfolio objects that exist in the database
     */
    public List<Portfolio> getPortfolios(){

        return portfolioRepo.findAll();
    }
}
