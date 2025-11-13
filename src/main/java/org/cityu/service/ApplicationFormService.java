package org.cityu.service;

import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.error.BusinessException;
import org.cityu.service.model.ApplicationFormModel;

public interface ApplicationFormService {
    void createApplicationForm(ApplicationFormModel applicationFormModel) throws BusinessException;
    ApplicationFormModel getApplicationForm(String applicationFormNumber);
}
