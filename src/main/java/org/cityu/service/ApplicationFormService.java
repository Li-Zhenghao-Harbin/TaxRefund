package org.cityu.service;

import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.service.model.ApplicationFormModel;

public interface ApplicationFormService {
    void createApplicationForm(ApplicationFormModel applicationFormModel);
    ApplicationFormModel getApplicationForm(String applicationFormNumber);
}
