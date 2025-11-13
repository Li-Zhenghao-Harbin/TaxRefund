package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.service.model.InvoiceModel;

public interface InvoiceMapper {
    int insert(InvoiceDO record);
    int updateInvoiceToRelatedApplicationForm(String applicationFormNumber, List<String> invoiceNumbers);
    List<InvoiceDO> getInvoiceByApplicationFormNumber(String applicationFormNumber);
    InvoiceDO getInvoiceByInvoiceNumber(String invoiceNumber);
}