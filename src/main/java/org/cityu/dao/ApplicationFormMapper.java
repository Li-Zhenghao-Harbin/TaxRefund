package org.cityu.dao;

import org.cityu.dataobject.ApplicationFormDO;

public interface ApplicationFormMapper {
    int insert(ApplicationFormDO record);
    ApplicationFormDO getApplicationForm(String applicationFormNumber);
}