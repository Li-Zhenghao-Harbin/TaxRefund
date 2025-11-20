package org.cityu.service;

import org.cityu.error.BusinessException;
import org.cityu.service.model.TaxRefundModel;

public interface TaxRefundService {
    void taxRefundByCash(TaxRefundModel taxRefundModel) throws BusinessException;
    void taxRefundByBankCard(TaxRefundModel taxRefundModel) throws BusinessException;
}
