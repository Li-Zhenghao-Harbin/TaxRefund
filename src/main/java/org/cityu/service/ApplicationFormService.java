package org.cityu.service;

import org.cityu.error.BusinessException;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.ItemModel;

import java.math.BigDecimal;
import java.util.List;

public interface ApplicationFormService {
    void createApplicationForm(ApplicationFormModel applicationFormModel) throws BusinessException;
    ApplicationFormModel getApplicationForm(String applicationFormNumber) throws BusinessException;
    void reviewApplicationForm(String applicationFormNumber, List<ItemModel> items) throws BusinessException;
}