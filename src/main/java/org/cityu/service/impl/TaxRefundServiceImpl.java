package org.cityu.service.impl;

import org.cityu.dao.ApplicationFormMapper;
import org.cityu.dao.BankCardMapper;
import org.cityu.dao.TaxRefundMapper;
import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.dataobject.BankCardDO;
import org.cityu.dataobject.TaxRefundDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.TaxRefundService;
import org.cityu.service.model.TaxRefundModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaxRefundServiceImpl implements TaxRefundService {

    @Autowired
    private TaxRefundMapper taxRefundMapper;

    @Autowired
    private ApplicationFormMapper applicationFormMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void taxRefundByCash(TaxRefundModel taxRefundModel) throws BusinessException {
        commonTaxRefund(taxRefundModel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void taxRefundByBankCard(TaxRefundModel taxRefundModel) throws BusinessException {
        commonTaxRefund(taxRefundModel);
        // insert bank card info
        List<String> applicationFormNumbers = taxRefundModel.getApplicationFormNumbers();
        for (String applicationFormNumber : applicationFormNumbers) {
            BankCardDO bankCardDO = new BankCardDO();
            bankCardDO.setApplicationFormNumber(applicationFormNumber);
            bankCardDO.setBankCardNumber(taxRefundModel.getBankCardNumber());
            bankCardDO.setBankCardHolder(taxRefundModel.getBankCardHolder());
            bankCardDO.setBankName(taxRefundModel.getBankName());
            bankCardMapper.insert(bankCardDO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void commonTaxRefund(TaxRefundModel taxRefundModel) throws BusinessException {
        List<String> applicationFormNumbers = taxRefundModel.getApplicationFormNumbers();
        for (String applicationFormNumber : applicationFormNumbers) {
            // check application form status
            ApplicationFormDO applicationFormDO = applicationFormMapper.getApplicationFormByApplicationFormNumber(applicationFormNumber);
            if (applicationFormDO == null) {
                throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_EXIST);
            }
            if (applicationFormDO.getStatus() == 1) {
                throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_REVIEWED);
            }
            if (applicationFormDO.getStatus() == 3) {
                throw new BusinessException(EmBusinessError.REPEAT_TAX_REFUND);
            }
            // insert tax refund record
            TaxRefundDO taxRefundDO = convertFromTaxRefundModel(taxRefundModel);
            taxRefundDO.setApplicationFormNumber(applicationFormNumber);
            taxRefundMapper.insert(taxRefundDO);
            // update application form
            applicationFormMapper.taxRefundApplicationForm(applicationFormNumber);
        }
    }

    private TaxRefundDO convertFromTaxRefundModel(TaxRefundModel taxRefundModel) {
        TaxRefundDO taxRefundDO = new TaxRefundDO();
        BeanUtils.copyProperties(taxRefundModel, taxRefundDO);
        return taxRefundDO;
    }
}