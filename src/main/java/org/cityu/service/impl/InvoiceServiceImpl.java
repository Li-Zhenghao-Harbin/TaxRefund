package org.cityu.service.impl;

import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.ItemMapper;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.dataobject.ItemDO;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    @Transactional
    public void createInvoice(InvoiceModel invoiceModel) {
        InvoiceDO invoiceDO = convertFromInvoiceModel(invoiceModel);
        invoiceMapper.insert(invoiceDO);
        for (ItemModel itemModel : invoiceModel.getItems()) {
            itemMapper.insert(convertFromItemModel(itemModel));
        }
    }

    private InvoiceDO convertFromInvoiceModel(InvoiceModel invoiceModel) {
        InvoiceDO invoiceDO = new InvoiceDO();
        BeanUtils.copyProperties(invoiceModel, invoiceDO);
        return invoiceDO;
    }

    private ItemDO convertFromItemModel(ItemModel itemModel) {
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        return itemDO;
    }
}
