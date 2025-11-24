package org.cityu.dao;

import org.cityu.dataobject.ApplicationFormDO;

import java.math.BigDecimal;
import java.util.List;

public interface ApplicationFormMapper {
    int insert(ApplicationFormDO record);
    ApplicationFormDO getApplicationFormByApplicationNumber(String applicationFormNumber);
    List<ApplicationFormDO> getApplicationFormsByIssueMerchantName(String issueMerchantName);
    List<ApplicationFormDO> getAllApplicationForms();
    int updateReviewedApplicationForm(String applicationFormNumber, BigDecimal customsConfirmAmount, Integer status);
    BigDecimal calculateCustomsConfirmAmount(String applicationFormNumber);
    BigDecimal calculateTotalAmount(List<String> invoiceNumbers);
    int taxRefundApplicationForm(String applicationFormNumber);
}