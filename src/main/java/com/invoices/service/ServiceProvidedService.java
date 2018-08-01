package com.invoices.service;

import com.invoices.domain.ServiceProvided;
import com.invoices.repository.ServiceProvidedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author psoutzis
 * This class is annotated as a service.
 * It is the service bean for the ServiceProvided entity
 */
@Service
public class ServiceProvidedService {
    @Autowired
    private ServiceProvidedRepo serviceProvidedRepo;

    public ServiceProvided getRecord(Long id){

        return serviceProvidedRepo.findServiceProvidedById(id);
    }

    public List<ServiceProvided> getServicesProvided() {

        return serviceProvidedRepo.findAll();
    }
}
