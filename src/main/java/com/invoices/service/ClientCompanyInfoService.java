package com.invoices.service;

import com.invoices.domain.ClientCompanyInfo;
import com.invoices.dto.UpdateInvoiceDTO;
import com.invoices.repository.ClientCompanyInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientCompanyInfoService {

    @Autowired ClientCompanyInfoRepo clientCompanyInfoRepo;

    public ClientCompanyInfo getRecordAndSave(ClientCompanyInfo companyInfo){
        if(clientCompanyInfoRepo.findClientCompanyInfoByVatNumber(companyInfo.getVatNumber()) == null)
            clientCompanyInfoRepo.save(companyInfo);
        else
            companyInfo = clientCompanyInfoRepo.findClientCompanyInfoByVatNumber(companyInfo.getVatNumber());

        return companyInfo;
    }

    public void setClientCompanyInfoFields(ClientCompanyInfo clientCompanyInfo, UpdateInvoiceDTO updateInvoiceDTO)
    {
        if(updateInvoiceDTO.getCompanyName() != null) {
            clientCompanyInfo.setAddress(updateInvoiceDTO.getCompanyAddress());
            clientCompanyInfo.setCity(updateInvoiceDTO.getCompanyCity());
            clientCompanyInfo.setName(updateInvoiceDTO.getCompanyName());
            clientCompanyInfo.setPostcode(updateInvoiceDTO.getCompanyPostcode());
            clientCompanyInfo.setVatNumber(updateInvoiceDTO.getCompanyVatNumber());
        }
    }
}
