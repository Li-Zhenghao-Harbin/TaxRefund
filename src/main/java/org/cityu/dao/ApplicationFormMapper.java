package org.cityu.dao;

import org.cityu.dataobject.ApplicationFormDO;

import java.math.BigDecimal;
import java.util.Map;

public interface ApplicationFormMapper {
    int insert(ApplicationFormDO record);
    ApplicationFormDO getApplicationForm(String applicationFormNumber);
    int updateReviewedApplicationForm(String applicationFormNumber, BigDecimal customsConfirmAmount, Integer status);
    BigDecimal calculateCustomsConfirmAmount(String applicationFormNumber);
}