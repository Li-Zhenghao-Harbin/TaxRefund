package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.TaxRefundService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.TaxRefundModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.cityu.common.utils.JsonUtils.getApplicationFormNumbersFromJson;

@RestController
@RequestMapping("/taxRefund")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class TaxRefundController extends BaseController {

    @Autowired
    private TaxRefundService taxRefundService;

    @RequestMapping(value = "/taxRefundByCash", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_JSON})
    @ResponseBody
    @RequireRole({ROLE_AGENCY})
    public CommonReturnType taxRefundByCash(@RequestBody JsonNode jsonNode) throws BusinessException {
        JsonNode applicationFormNumbersNode = jsonNode.get("applicationFormNumbers");
        List<String> applicationFormNumbers = getApplicationFormNumbersFromJson(applicationFormNumbersNode);

        TaxRefundModel taxRefundModel = new TaxRefundModel();
        taxRefundModel.setApplicationFormNumbers(applicationFormNumbers);
        taxRefundModel.setTaxRefundMethod(1);
        taxRefundModel.setTaxRefundDate(CommonUtils.getCurrentDate());
        taxRefundModel.setApplicationFormMaterial(CommonUtils.generateApplicationFormMaterial(applicationFormNumbers.get(0)));
        taxRefundModel.setInvoiceMaterial(CommonUtils.generateInvoiceMaterial(applicationFormNumbers.get(0)));
        taxRefundService.taxRefundByCash(taxRefundModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/taxRefundByBankCard", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_JSON})
    @ResponseBody
    @RequireRole({ROLE_AGENCY})
    public CommonReturnType taxRefundByBankCard(@RequestBody JsonNode jsonNode) throws BusinessException {
        JsonNode applicationFormNumbersNode = jsonNode.get("applicationFormNumbers");
        List<String> applicationFormNumbers = getApplicationFormNumbersFromJson(applicationFormNumbersNode);
        String bankCardNumber = jsonNode.get("bankCardNumber").asText();
        String bankCardHolder = jsonNode.get("bankCardHolder").asText();
        String bankName = jsonNode.get("bankName").asText();

        TaxRefundModel taxRefundModel = new TaxRefundModel();
        taxRefundModel.setApplicationFormNumbers(applicationFormNumbers);
        taxRefundModel.setTaxRefundMethod(2);
        taxRefundModel.setTaxRefundDate(CommonUtils.getCurrentDate());
        taxRefundModel.setApplicationFormMaterial(CommonUtils.generateApplicationFormMaterial(applicationFormNumbers.get(0)));
        taxRefundModel.setInvoiceMaterial(CommonUtils.generateInvoiceMaterial(applicationFormNumbers.get(0)));
        taxRefundModel.setBankCardNumber(bankCardNumber);
        taxRefundModel.setBankCardHolder(bankCardHolder);
        taxRefundModel.setBankName(bankName);
        taxRefundService.taxRefundByBankCard(taxRefundModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/printReceipt", method = {RequestMethod.POST})
    @ResponseBody
    @RequireRole({ROLE_AGENCY})
    public CommonReturnType printReceipt() {
        return CommonReturnType.create("Receipt has been printed");
    }
}
