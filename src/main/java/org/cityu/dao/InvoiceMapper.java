package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.InvoiceDO;

public interface InvoiceMapper {
    int insert(InvoiceDO record);
    int updateInvoiceToRelatedApplicationForm(String applicationFormNumber, List<String> invoiceNumbers);
    List<InvoiceDO> getInvoiceByApplicationFormNumber(String applicationFormNumber);
    InvoiceDO getInvoiceByInvoiceNumber(String invoiceNumber);
}