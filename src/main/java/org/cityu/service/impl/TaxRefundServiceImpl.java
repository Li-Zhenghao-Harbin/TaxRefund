package org.cityu.service.impl;

import org.cityu.dao.ApplicationFormMapper;
import org.cityu.dao.TaxRefundMapper;
import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.dataobject.TaxRefundDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.TaxRefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaxRefundServiceImpl implements TaxRefundService {

    @Autowired
    private TaxRefundMapper taxRefundMapper;

    @Autowired
    private ApplicationFormMapper applicationFormMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void taxRefundByCash(TaxRefundDO taxRefundDO) throws BusinessException {
        String applicationFormNumber = taxRefundDO.getApplicationFormNumber();
        // check application form status
        ApplicationFormDO applicationFormDO = applicationFormMapper.getApplicationForm(applicationFormNumber);
        if (applicationFormDO == null) {
            throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_EXIST);
        }
        if (applicationFormDO.getStatus() == 1) {
            throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_REVIEWED);
        }
        if (applicationFormDO.getStatus() == 3) {
            throw new BusinessException(EmBusinessError.REPEAT_TAX_REFOUND);
        }
        // insert tax refund record
        taxRefundMapper.insert(taxRefundDO);
        // update application form
        applicationFormMapper.taxRefundApplicationForm(applicationFormNumber);
    }
}