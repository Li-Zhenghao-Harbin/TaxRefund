package org.cityu.service.impl;

import org.cityu.common.component.UserContext;
import org.cityu.dao.ApplicationFormMapper;
import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.ItemMapper;
import org.cityu.dao.SequenceMapper;
import org.cityu.dataobject.ApplicationFormDO;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.dataobject.ItemDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.cityu.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.common.utils.CommonUtils.generateApplicationFormNumber;
import static org.cityu.controller.BaseController.BUSINESS_APPLICATION_FORM;

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
        // get merchant id
        UserModel currentUser = UserContext.getCurrentUser();
        applicationFormDO.setIssueMerchantName(currentUser.getName());
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
                throw new BusinessException(EmBusinessError.IMPROPER_INVOICE_STATUS);
            }
            invoiceNumbers.add(invoiceModel.getInvoiceNumber());
        }
        // calculate total amount
        BigDecimal totalAmount = applicationFormMapper.calculateTotalAmount(invoiceNumbers);
        if (totalAmount.compareTo(BigDecimal.valueOf(200)) < 0) {
            throw new BusinessException(EmBusinessError.IMPROPER_APPLICATION_AMOUNT, "Total amount should not less than ￥200");
        }
        applicationFormDO.setTotalAmount(totalAmount);
        applicationFormMapper.insert(applicationFormDO);
        // update application form information
        invoiceMapper.updateInvoiceToRelatedApplicationForm(applicationFormNumber, invoiceNumbers);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ApplicationFormModel getApplicationFormByApplicationFormNumber(String applicationFormNumber) throws BusinessException {
        ApplicationFormDO applicationFormDO = applicationFormMapper.getApplicationFormByApplicationFormNumber(applicationFormNumber);
        if (applicationFormDO == null) {
            throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_EXIST);
        }
        ApplicationFormModel applicationFormModel = convertFormApplicationFormDO(applicationFormDO);
        List<InvoiceModel> invoices = invoiceService.getInvoiceByApplicationFormNumber(applicationFormNumber);
        applicationFormModel.setInvoices(invoices);
        return applicationFormModel;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ApplicationFormModel> getApplicationFormsByIssueMerchantName(String issueMerchantName) throws BusinessException {
        List<ApplicationFormDO> applicationFormDOs = applicationFormMapper.getApplicationFormsByIssueMerchantName(issueMerchantName);
        return getApplicationFormModels(applicationFormDOs);
    }

    @Transactional
    private List<ApplicationFormModel> getApplicationFormModels(List<ApplicationFormDO> applicationFormDOs) throws BusinessException {
        if (applicationFormDOs.isEmpty()) {
            return null;
        }
        List<ApplicationFormModel> applicationFormModels = convertFromApplicationFormDOs(applicationFormDOs);
        for  (ApplicationFormModel applicationFormModel : applicationFormModels) {
            String applicationFormNumber = applicationFormModel.getApplicationFormNumber();
            List<InvoiceModel> invoices = invoiceService.getInvoiceByApplicationFormNumber(applicationFormNumber);
            applicationFormModel.setInvoices(invoices);
        }
        return applicationFormModels;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ApplicationFormModel> getApplicationFormsByApplicant(String applicantName, String applicantId, String applicantCountry) throws BusinessException {
        List<ApplicationFormDO> applicationFormDOs = applicationFormMapper.getApplicationFormsByApplicant(applicantName, applicantId, applicantCountry);
        return getApplicationFormModels(applicationFormDOs);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<ApplicationFormModel> getAllApplicationForms() throws BusinessException {
        List<ApplicationFormDO> applicationFormDOs = applicationFormMapper.getAllApplicationForms();
        return getApplicationFormModels(applicationFormDOs);
    }

    private List<ApplicationFormModel> convertFromApplicationFormDOs(List<ApplicationFormDO> applicationFormDOs) {
        List<ApplicationFormModel> applicationFormModels = new ArrayList<>();
        for (ApplicationFormDO applicationFormDO : applicationFormDOs) {
            ApplicationFormModel applicationFormModel = new ApplicationFormModel();
            BeanUtils.copyProperties(applicationFormDO, applicationFormModel);
            applicationFormModels.add(applicationFormModel);
        }
        return applicationFormModels;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void reviewApplicationForm(String applicationFormNumber, List<ItemModel> rejectedItems) throws BusinessException {
        ApplicationFormDO applicationFormDO = applicationFormMapper.getApplicationFormByApplicationFormNumber(applicationFormNumber);
        if (applicationFormDO == null) {
            throw new BusinessException(EmBusinessError.APPLICATION_FORM_NOT_EXIST);
        }
        List<InvoiceDO> invoiceDOs = invoiceMapper.getInvoiceByApplicationFormNumber(applicationFormNumber);
        // approve items
        for (InvoiceDO invoiceDO : invoiceDOs) {
            List<ItemDO> itemDOs = itemMapper.getItemByInvoiceNumber(invoiceDO.getInvoiceNumber());
            for (ItemDO itemDO : itemDOs) {
                // check item status
                if (itemDO.getStatus() != 1) {
                    throw new BusinessException(EmBusinessError.IMPROPER_APPLICATION_STATUS);
                }
                itemMapper.updateItemStatus(itemDO.getId(), 2);
            }
        }
        // reject items
        for (ItemModel itemModel : rejectedItems) {
            itemMapper.updateItemStatus(itemModel.getId(), -1);
        }
        BigDecimal customsConfirmAmount = applicationFormMapper.calculateCustomsConfirmAmount(applicationFormNumber);
        applicationFormMapper.updateReviewedApplicationForm(applicationFormNumber, customsConfirmAmount, 2);
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
