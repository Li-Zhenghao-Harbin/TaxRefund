package org.cityu.service;

import org.cityu.dataobject.TaxRefundDO;
import org.cityu.error.BusinessException;

public interface TaxRefundService {
    void taxRefundByCash(TaxRefundDO taxRefundDO) throws BusinessException;
}
