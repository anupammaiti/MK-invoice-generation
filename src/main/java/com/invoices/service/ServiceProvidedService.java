package com.invoices.service;

import com.invoices.domain.ServiceProvided;
import com.invoices.repository.ServiceProvidedRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is annotated as a service.
 * It is the service bean for the ServiceProvided entity
 * @author psoutzis
 */
@Service
public class ServiceProvidedService {
    @Autowired
    private ServiceProvidedRepo serviceProvidedRepo;

    /**
     * @param id The primary key of the record to return
     * @return The ServiceProvided object whose id is equal to the method argument
     */
    public ServiceProvided getRecord(Long id){

        return serviceProvidedRepo.findServiceProvidedById(id);
    }

    /**
     * @return A collection of all the ServiceProvided objects that exist in the database
     */
    public List<ServiceProvided> getServicesProvided() {

        return serviceProvidedRepo.findAll();
    }
}
