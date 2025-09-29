package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.InvoiceDO;

public interface InvoiceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(InvoiceDO record);

    InvoiceDO selectByPrimaryKey(Integer id);

    List<InvoiceDO> selectAll();

    int updateByPrimaryKey(InvoiceDO record);
}