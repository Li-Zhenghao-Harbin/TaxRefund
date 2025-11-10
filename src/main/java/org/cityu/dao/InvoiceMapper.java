package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.service.model.InvoiceModel;

public interface InvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvoiceDO record);

    InvoiceDO selectByPrimaryKey(Integer id);

    List<InvoiceDO> selectAll();

    int updateByPrimaryKey(InvoiceDO record);

    int updateInvoiceToRelatedApplicationForm(String applicationFormNumber, List<String> invoiceNumbers);
}