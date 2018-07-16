package com.invoices.service;

import com.invoices.domain.ServiceProvided;
import com.invoices.repository.ServiceProvidedRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProvidedService {
    @Autowired
    private ServiceProvidedRepo serviceProvidedRepo;
    @Getter @Setter
    private ServiceProvided serviceProvided;

    public ServiceProvided getRecord(ServiceProvided serviceProvided){

        return serviceProvidedRepo.findServiceProvidedByServiceName(serviceProvided.getServiceName());
    }

    public List<ServiceProvided> getAllServicesProvided() {

        return serviceProvidedRepo.findAll();
    }
}
