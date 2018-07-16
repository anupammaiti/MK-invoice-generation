package com.invoices.service;

import com.invoices.domain.Portfolio;
import com.invoices.repository.PortfolioRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {
    @Autowired
    private PortfolioRepo portfolioRepo;

    @Getter @Setter
    private Portfolio portfolio;

    public Portfolio getRecord(Portfolio portfolio){

        return portfolioRepo.getPortfolioByPortfolioCode(portfolio.getPortfolioCode());
    }

    public List<Portfolio> getPortfolios(){

        return portfolioRepo.findAll();
    }
}
