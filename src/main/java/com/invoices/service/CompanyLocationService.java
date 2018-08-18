package com.invoices.service;

import com.invoices.domain.CompanyLocation;
import com.invoices.repository.CompanyLocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * This class is the service responsible for CompanyLocation-type objects
 * @author psoutzis
 */
@Service
public class CompanyLocationService {

    @Autowired CompanyLocationRepo companyLocationRepo;

    /**
     * @return An alphabetically sorted collection (ArrayList) of all the known countries in the world.
     */
    public ArrayList<String> getCountriesList(){
        ArrayList<String> countries = new ArrayList<>();
        String[] countryCodes = Locale.getISOCountries();
        for(String code : countryCodes){
            Locale locale = new Locale("", code);
            String country = locale.getDisplayCountry().toUpperCase();
            countries.add(country);
        }
        Collections.sort(countries);

        return countries;
    }

    /**
     *
     * @param country The country-name to add to the database
     * @return The (saved) CompanyLocation-type object that was just inserted, representing the country
     * that was passed as an argument.
     */
    public CompanyLocation getRecordAndSave(String country){
        country = country.toUpperCase();
        CompanyLocation location = companyLocationRepo.findCompanyLocationByCountry(country);
        if(location == null)
        {
            location = new CompanyLocation(null, country);
            companyLocationRepo.save(location);
        }

        return location;
    }
}
