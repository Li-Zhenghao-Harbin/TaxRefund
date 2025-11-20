package org.cityu.controller;

import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.dataobject.TaxRefundDO;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.TaxRefundService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.cityu.controller.BaseController.*;

@RestController
@RequestMapping("/taxRefund")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class TaxRefundController {

    @Autowired
    private TaxRefundService taxRefundService;

    @RequestMapping(value = "/taxRefundByCash", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType taxRefundByCash(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) throws BusinessException {
        TaxRefundDO taxRefundDO = new TaxRefundDO();
        taxRefundDO.setApplicationFormNumber(applicationFormNumber);
        taxRefundDO.setTaxRefundMethod(1);
        taxRefundDO.setTaxRefundDate(CommonUtils.getCurrentDate());
        taxRefundDO.setApplicationFormMaterial(CommonUtils.generateApplicationFormMaterial(applicationFormNumber));
        taxRefundDO.setInvoiceMaterial(CommonUtils.generateInvoiceMaterial(applicationFormNumber));
        taxRefundService.taxRefundByCash(taxRefundDO);
        return CommonReturnType.create(null);
    }
}
