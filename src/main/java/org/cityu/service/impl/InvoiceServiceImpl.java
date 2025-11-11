package org.cityu.service.impl;

import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.ItemMapper;
import org.cityu.dao.SequenceMapper;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.dataobject.ItemDO;
import org.cityu.error.BusinessException;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.cityu.common.utils.CommonUtils.generateInvoiceNumber;
import static org.cityu.controller.BaseController.BUSINESS_INVOICE;

@Service
public class InvoiceServiceImpl implements InvoiceService {
    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private SequenceMapper sequenceMapper;

    @Override
    @Transactional
    public void createInvoice(InvoiceModel invoiceModel) {
        // generate invoice number
        int invoiceNumberSeries = sequenceMapper.getCurrentValue(BUSINESS_INVOICE);
        sequenceMapper.updateCurrentValue(BUSINESS_INVOICE);
        String invoiceNumber = generateInvoiceNumber(invoiceNumberSeries);
        // reset invoice number
        invoiceModel.setInvoiceNumber(invoiceNumber);
        InvoiceDO invoiceDO = convertFromInvoiceModel(invoiceModel);
        invoiceMapper.insert(invoiceDO);
        for (ItemModel itemModel : invoiceModel.getItems()) {
            itemModel.setInvoiceNumber(invoiceNumber);
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
