package org.cityu.service.impl;

import org.cityu.common.component.UserContext;
import org.cityu.dao.InvoiceMapper;
import org.cityu.dao.ItemMapper;
import org.cityu.dao.SellerMapper;
import org.cityu.dao.SequenceMapper;
import org.cityu.dataobject.InvoiceDO;
import org.cityu.dataobject.ItemDO;
import org.cityu.dataobject.SellerDO;
import org.cityu.error.BusinessException;
import org.cityu.error.EmBusinessError;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.cityu.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    @Transactional
    public void createInvoice(InvoiceModel invoiceModel) {
        // generate invoice number
        int invoiceNumberSeries = sequenceMapper.getCurrentValue(BUSINESS_INVOICE);
        sequenceMapper.updateCurrentValue(BUSINESS_INVOICE);
        String invoiceNumber = generateInvoiceNumber(invoiceNumberSeries);
        // set items
        BigDecimal totalAmount = new BigDecimal(0);
        for (ItemModel itemModel : invoiceModel.getItems()) {
            itemModel.setInvoiceNumber(invoiceNumber);
            itemMapper.insert(convertFromItemModel(itemModel));
            // calculate total amount
            totalAmount = totalAmount.add(itemModel.getUnitPrice().multiply(BigDecimal.valueOf(itemModel.getQuantity())));
        }
        // set invoice properties
        invoiceModel.setInvoiceNumber(invoiceNumber);
        InvoiceDO invoiceDO = convertFromInvoiceModel(invoiceModel);
        invoiceDO.setTotalAmount(totalAmount);
        // set seller tax id
        UserModel currentUser = UserContext.getCurrentUser();
        invoiceDO.setIssueMerchantName(currentUser.getName());
        invoiceMapper.insert(invoiceDO);
    }

    @Override
    public void discardInvoice(String invoiceNumber) {
        invoiceMapper.discardInvoice(invoiceNumber);
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public List<InvoiceModel> getInvoiceByApplicationFormNumber(String applicationFormNumber) throws BusinessException {
        List<InvoiceDO> invoiceDOs = invoiceMapper.getInvoiceByApplicationFormNumber(applicationFormNumber);
        if (invoiceDOs.isEmpty()) {
            throw new BusinessException(EmBusinessError.INVOICE_NOT_EXIST);
        }
        List<InvoiceModel> invoiceModels = convertFromInvoiceDOList(invoiceDOs);
        for (InvoiceModel invoiceModel : invoiceModels) {
            String invoiceNumber = invoiceModel.getInvoiceNumber();
            List<ItemDO> itemDOs = itemMapper.getItemByInvoiceNumber(invoiceNumber);
            List<ItemModel> itemModels = convertFromItemDOList(itemDOs);
            invoiceModel.setItems(itemModels);
        }
        return invoiceModels;
    }

    private List<InvoiceModel> convertFromInvoiceDOList(List<InvoiceDO> invoiceDOs) {
        List<InvoiceModel> invoiceModels = new ArrayList<>();
        for (InvoiceDO invoiceDO : invoiceDOs) {
            InvoiceModel invoiceModel = convertFromInvoiceDO(invoiceDO);
            invoiceModels.add(invoiceModel);
        }
        return invoiceModels;
    }

    @Override
    @Transactional(rollbackFor =  Exception.class)
    public InvoiceModel getInvoiceByInvoiceNumber(String invoiceNumber) throws BusinessException {
        InvoiceDO invoiceDO = invoiceMapper.getInvoiceByInvoiceNumber(invoiceNumber);
        if (invoiceDO == null) {
            throw new BusinessException(EmBusinessError.INVOICE_NOT_EXIST);
        }
        InvoiceModel invoiceModel = convertFromInvoiceDO(invoiceDO);
        List<ItemModel> items = convertFromItemDOList(itemMapper.getItemByInvoiceNumber(invoiceNumber));
        invoiceModel.setItems(items);
        return invoiceModel;
    }

    @Override
    public List<InvoiceModel> getInvoicesByIssueMerchantName(String issueMerchantName) {
        List<InvoiceDO> invoiceDOs = invoiceMapper.getInvoicesByIssueMerchantName(issueMerchantName);
        if (invoiceDOs.isEmpty()) {
            return null;
        }
        List<InvoiceModel> invoiceModels = convertFromInvoiceDOList(invoiceDOs);
        for (InvoiceModel invoiceModel : invoiceModels) {
            String invoiceNumber = invoiceModel.getInvoiceNumber();
            List<ItemDO> itemDOs = itemMapper.getItemByInvoiceNumber(invoiceNumber);
            List<ItemModel> itemModels = convertFromItemDOList(itemDOs);
            invoiceModel.setItems(itemModels);
        }
        return invoiceModels;
    }

    private List<ItemModel> convertFromItemDOList(List<ItemDO> itemDOS) {
        List<ItemModel> itemModels = new ArrayList<>();
        for (ItemDO itemDO : itemDOS) {
            itemModels.add(convertFromItemDO(itemDO));
        }
        return itemModels;
    }

    private ItemModel convertFromItemDO(ItemDO itemDO) {
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        return itemModel;
    }

    private InvoiceModel convertFromInvoiceDO(InvoiceDO invoiceDO) {
        InvoiceModel invoiceModel = new InvoiceModel();
        BeanUtils.copyProperties(invoiceDO, invoiceModel);
        return invoiceModel;
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
