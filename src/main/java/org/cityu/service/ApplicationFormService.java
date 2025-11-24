package org.cityu.service;

import org.cityu.error.BusinessException;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.ItemModel;

import java.util.List;

public interface ApplicationFormService {
    void createApplicationForm(ApplicationFormModel applicationFormModel) throws BusinessException;
    ApplicationFormModel getApplicationFormByApplicationFormNumber(String applicationFormNumber) throws BusinessException;
    List<ApplicationFormModel> getApplicationFormsByIssueMerchantName(String issueMerchantName) throws BusinessException;
    List<ApplicationFormModel> getAllApplicationForms() throws BusinessException;
    void reviewApplicationForm(String applicationFormNumber, List<ItemModel> rejectedItems) throws BusinessException;
}