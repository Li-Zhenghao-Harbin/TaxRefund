package org.cityu.service;

import org.cityu.dataobject.InvoiceDO;
import org.cityu.service.model.InvoiceModel;

import java.util.List;

public interface InvoiceService {
    void createInvoice(InvoiceModel invoiceModel);
    List<InvoiceModel> getInvoiceByApplicationFormNumber(String applicationFormNumber);
    InvoiceModel getInvoiceByInvoiceNumber(String invoiceNumber);
}
