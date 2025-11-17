package org.cityu.service;

import org.cityu.dataobject.InvoiceDO;
import org.cityu.error.BusinessException;
import org.cityu.service.model.InvoiceModel;

import java.util.List;

public interface InvoiceService {
    void createInvoice(InvoiceModel invoiceModel);
    List<InvoiceModel> getInvoiceByApplicationFormNumber(String applicationFormNumber) throws BusinessException;
    InvoiceModel getInvoiceByInvoiceNumber(String invoiceNumber) throws BusinessException;
}
