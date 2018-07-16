package com.invoices.service;

import com.invoices.domain.Portfolio;
import com.invoices.repository.PortfolioRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the Portfolio entity
 */
@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepo portfolioRepo;

    @Getter @Setter
    private Portfolio portfolio;

    public Portfolio getRecord(Long id){

        return portfolioRepo.getPortfolioById(id);
    }

    public List<Portfolio> getPortfolios(){

        return portfolioRepo.findAll();
    }
}
