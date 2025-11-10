package org.cityu.service.impl;

import org.cityu.dao.ApplicationFormMapper;
import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.SequenceMapper;
import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.cityu.common.utils.CommonUtils.generateApplicationFormNumber;
import static org.cityu.common.utils.CommonUtils.generateInvoiceNumber;
import static org.cityu.controller.BaseController.BUSINESS_APPLICATION_FORM;
import static org.cityu.controller.BaseController.BUSINESS_INVOICE;

@Service
public class ApplicationFormServiceImpl implements ApplicationFormService {
    @Autowired
    private ApplicationFormMapper applicationFormMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Override
    @Transactional
    public void createApplicationForm(ApplicationFormModel applicationFormModel) {
        // generate application form
        int invoiceNumberSeries = sequenceMapper.getCurrentValue(BUSINESS_APPLICATION_FORM);
        sequenceMapper.updateCurrentValue(BUSINESS_APPLICATION_FORM);
        String applicationFormNumber = generateApplicationFormNumber(invoiceNumberSeries);
        // create application form
        ApplicationFormDO applicationFormDO = convertFromApplicationFormModel(applicationFormModel);
        applicationFormDO.setApplicationFormNumber(applicationFormNumber);
        applicationFormMapper.insert(applicationFormDO);
        // relate to invoices
        List<InvoiceModel> invoices = applicationFormModel.getInvoices();
        List<String> invoiceNumbers = new ArrayList<>();
        for (InvoiceModel invoiceModel : invoices) {
            invoiceNumbers.add(invoiceModel.getInvoiceNumber());
        }
        invoiceMapper.updateInvoiceToRelatedApplicationForm(applicationFormNumber, invoiceNumbers);
    }

    private ApplicationFormDO convertFromApplicationFormModel(ApplicationFormModel applicationFormModel) {
        ApplicationFormDO applicationFormDO = new ApplicationFormDO();
        BeanUtils.copyProperties(applicationFormModel, applicationFormDO);
        return applicationFormDO;
    }
}
