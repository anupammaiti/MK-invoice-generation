package com.invoices.service;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.repository.ClientCompanyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author psoutzis
 * Service that will handle mainly changes, made to an invoice's ClientCompanyInfo object.
 */
@Service
public class ClientCompanyInfoService {

    @Autowired ClientCompanyInfoRepo clientCompanyInfoRepo;

    /**
     * Method that fetches record from database with specified id
     * @param id the id of the record to fetch
     * @return the record with a unique-id identical to the id passed as argument
     */
    public ClientCompanyInfo getRecord(Long id){

        return clientCompanyInfoRepo.findClientCompanyInfoById(id);
    }

    /**
     * This method will compare a ClientCompanyInfo's attributes(variables) with a DTO's, to
     * find differences. In case differences are found, the company will either be updated in the database
     * and on the invoice, or the new company will be stored in the database and will replace the current company
     * on the invoice. In case company is deleted, it will be removed from the invoice.
     * In the case that the current company is replaced by either a new company or is deleted entirely,
     * the program will keep the old company in the database for future reference(s).
     *
     * @param updateData the DTO that holds new data (or data to remain the same for that matter).
     * @param company the ClientCompanyInfo(company) object of the invoice to be updated.
     * @return the ClientCompanyInfo object to set to the updated invoice.
     */
    public ClientCompanyInfo detectChangesAndApply(UpdateInvoiceDTO updateData, ClientCompanyInfo company){
        String[] original = getCompanyAttributes(company);
        String[] toUpdate = getCompanyAttributes(updateData);
        boolean updateUnnecessary = Arrays.equals(original,toUpdate);

        if(!updateUnnecessary){
            company = setClientCompanyInfoFields(updateData, company);
            if(company != null) clientCompanyInfoRepo.save(company);//checking if company was deleted
        }

        return company;
    }


    /**
     * @param company the ClientCompanyInfo object of the invoice to be updated.
     * @param updateData The object containing updated values.
     * @return  a ClientCompanyInfo object with the updated values, or null if
     * a company does not exist for the issued invoice.
     */
    private ClientCompanyInfo setClientCompanyInfoFields(UpdateInvoiceDTO updateData, ClientCompanyInfo company){
        boolean isTheSameCompany = company.getVatNumber().equals(updateData.getCompanyVatNumber());
        boolean companyDeleted = (updateData.getCompanyVatNumber() == null);

        //If updates are about the company already passed to invoice and
        //the user did not completely remove the company from the invoice,
        //then update fields current record
        if(isTheSameCompany && !companyDeleted) {
            company.setName(updateData.getCompanyName());
            company.setVatNumber(updateData.getCompanyVatNumber());
            company.setAddress(updateData.getCompanyAddress());
            company.setCity(updateData.getCompanyCity());
            company.setPostcode(updateData.getCompanyPostcode());
        }
        //If VAT number indicates that this is a new company, then the
        //current record will not be deleted/replaced, but a new ClientCompanyInfo
        //object will be returned, so a new record can be added to the DB.
        else if(!isTheSameCompany && !companyDeleted){
            company = new ClientCompanyInfo(
                    null, updateData.getCompanyName(),
                    updateData.getCompanyVatNumber(),
                    updateData.getCompanyAddress(),
                    updateData.getCompanyCity(),
                    updateData.getCompanyPostcode(),
                    company.getClient(),
                    company.getCompanyLocation()
            );
        }

        //If company is deleted from invoice by the user, then null will be added to
        //the invoice's ClientCompanyInfo reference. This action is legal, considering that
        //invoices are issued to portfolios and not companies
        else if(companyDeleted)
            company = null;

        return company;
    }

    /**
     * @param company Object, whose certain values will be used to create an array for comparison purposes.
     * @return an Array containing values related to a ClientCompanyInfo object.
     * @throws NullPointerException when a null variable is accessed.
     */
    private String[] getCompanyAttributes(UpdateInvoiceDTO company)throws NullPointerException{
        return new String[] {
                company.getCompanyName(), company.getCompanyVatNumber(), company.getCompanyAddress(),
                company.getCompanyCity(), company.getCompanyPostcode(), company.getCompanyCountry()
        };
    }

    /**
     *
     * @param company ClientCompanyInfo object,whose values will be used to create an array for comparison purposes
     * @return an Array containing values that could be appended by a user. It will be used to compare previous
     * values of an invoice's ClientCompanyInfo object and the values inputted by the user.
     * @throws NullPointerException when a null variable is accessed.
     */
    private String[] getCompanyAttributes(ClientCompanyInfo company)throws NullPointerException{
        return new String[] {
                company.getName(), company.getVatNumber(), company.getAddress(),
                company.getCity(), company.getPostcode(), company.getCompanyLocation().getCountry()
        };
    }

}
