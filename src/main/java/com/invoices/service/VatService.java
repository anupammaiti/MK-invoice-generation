package com.invoices.service;

import com.invoices.domain.Vat;
import com.invoices.repository.VatRepo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.endsWith;

@Service
public class VatService {
    @Autowired
    private VatRepo vatRepo;

    @Getter@Setter
    private Vat vat;

    public Vat save(Vat vat){

        return vatRepo.save(vat);
    }

    public List<Vat> getVatRecords(){

        return vatRepo.findAll();
    }

    public Vat getVatById(Long id){
        return vatRepo.findVatByVatId(id);
    }

    /*
    public Vat getRecordWithRateOf(Vat vat){

        try {
            return vatRepo.findByVatRate(vat.getVatRate()-0.0001f, vat.getVatRate()+0.001f).get(0);
        }
        catch(NullPointerException e){
            e.printStackTrace();
            return new Vat();
        }
    }*/
}
