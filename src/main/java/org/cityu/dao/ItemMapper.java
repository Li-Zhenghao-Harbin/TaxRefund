package org.cityu.dao;

import java.util.List;
import org.cityu.dataobject.ItemDO;

public interface ItemMapper {
    int insert(ItemDO record);
    List<ItemDO> getItemByInvoiceNumber(String invoiceNumber);
    int updateItemStatus(Integer id, Integer status);
}