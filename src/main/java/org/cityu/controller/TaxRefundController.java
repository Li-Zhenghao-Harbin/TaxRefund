package org.cityu.controller;

import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.dataobject.TaxRefundDO;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.TaxRefundService;
import org.cityu.service.model.TaxRefundModel;
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
    @RequireRole({ROLE_AGENCY})
    public CommonReturnType taxRefundByCash(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) throws BusinessException {
        TaxRefundModel taxRefundModel = new TaxRefundModel();
        taxRefundModel.setApplicationFormNumber(applicationFormNumber);
        taxRefundModel.setTaxRefundMethod(1);
        taxRefundModel.setTaxRefundDate(CommonUtils.getCurrentDate());
        taxRefundModel.setApplicationFormMaterial(CommonUtils.generateApplicationFormMaterial(applicationFormNumber));
        taxRefundModel.setInvoiceMaterial(CommonUtils.generateInvoiceMaterial(applicationFormNumber));
        taxRefundService.taxRefundByCash(taxRefundModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/taxRefundByBankCard", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    @RequireRole({ROLE_AGENCY})
    public CommonReturnType taxRefundByBankCard(@RequestParam(name = "applicationFormNumber") String applicationFormNumber,
                                                @RequestParam(name = "bankCardNumber") String bankCardNumber,
                                                @RequestParam(name = "bankCardHolder") String bankCardHolder,
                                                @RequestParam(name = "bankName") String bankName) throws BusinessException {
        TaxRefundModel taxRefundModel = new TaxRefundModel();
        taxRefundModel.setApplicationFormNumber(applicationFormNumber);
        taxRefundModel.setTaxRefundMethod(2);
        taxRefundModel.setTaxRefundDate(CommonUtils.getCurrentDate());
        taxRefundModel.setApplicationFormMaterial(CommonUtils.generateApplicationFormMaterial(applicationFormNumber));
        taxRefundModel.setInvoiceMaterial(CommonUtils.generateInvoiceMaterial(applicationFormNumber));
        taxRefundModel.setBankCardNumber(bankCardNumber);
        taxRefundModel.setBankCardHolder(bankCardHolder);
        taxRefundModel.setBankName(bankName);
        taxRefundService.taxRefundByBankCard(taxRefundModel);
        return CommonReturnType.create(null);
    }
}
