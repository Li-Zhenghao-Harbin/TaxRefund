package org.cityu.service.impl;

import org.cityu.dao.ApplicationFormMapper;
import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.ItemMapper;
import org.cityu.dao.SequenceMapper;
import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createApplicationForm(ApplicationFormModel applicationFormModel) throws BusinessException {
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
        for (InvoiceModel invoice : invoices) {
            // set the status of the invoice
            InvoiceDO invoiceDO = invoiceMapper.getInvoiceByInvoiceNumber(invoice.getInvoiceNumber());
            invoice.setStatus(invoiceDO.getStatus());
        }
        List<String> invoiceNumbers = new ArrayList<>();
        for (InvoiceModel invoiceModel : invoices) {
            // check invoice
            if (invoiceModel.getStatus() != 1) {
                throw new BusinessException(EmBusinessError.INVOICE_STATUS_IMPROPER);
            }
            invoiceNumbers.add(invoiceModel.getInvoiceNumber());
        }
        // update application form information
        invoiceMapper.updateInvoiceToRelatedApplicationForm(applicationFormNumber, invoiceNumbers);
    }

    @Override
    @Transactional
    public ApplicationFormModel getApplicationForm(String applicationFormNumber) {
        ApplicationFormDO applicationFormDO = applicationFormMapper.getApplicationForm(applicationFormNumber);
        if (applicationFormDO == null) {
            return null;
        }
        ApplicationFormModel applicationFormModel = convertFormApplicationFormDO(applicationFormDO);
        List<InvoiceModel> invoices = invoiceService.getInvoiceByApplicationFormNumber(applicationFormNumber);
        applicationFormModel.setInvoices(invoices);
        return applicationFormModel;
    }

    @Override
    @Transactional
    public void reviewApplicationForm(String applicationFormNumber, List<ItemModel> items) throws BusinessException {
        for (ItemModel itemModel : items) {
            itemMapper.updateItemStatus(itemModel.getId(), -1);
        }
        applicationFormMapper.updateReviewedApplicationForm(applicationFormNumber, 2);
    }

    private ApplicationFormModel convertFormApplicationFormDO(ApplicationFormDO applicationFormDO) {
        ApplicationFormModel applicationFormModel = new ApplicationFormModel();
        BeanUtils.copyProperties(applicationFormDO, applicationFormModel);
        return applicationFormModel;
    }

    private ApplicationFormDO convertFromApplicationFormModel(ApplicationFormModel applicationFormModel) {
        ApplicationFormDO applicationFormDO = new ApplicationFormDO();
        BeanUtils.copyProperties(applicationFormModel, applicationFormDO);
        return applicationFormDO;
    }
}
