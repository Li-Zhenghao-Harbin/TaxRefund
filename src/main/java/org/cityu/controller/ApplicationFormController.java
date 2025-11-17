package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.dataobject.ItemDO;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.common.utils.JsonUtils.*;
import static org.cityu.controller.BaseController.*;

@RestController
@RequestMapping("/applicationForm")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class ApplicationFormController {
    @Autowired
    private ApplicationFormService applicationFormService;

    @RequestMapping(value = "/createApplicationForm", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_MERCHANT})
    public CommonReturnType createApplicationForm(@RequestBody JsonNode jsonNode) throws ParseException, BusinessException {
        String applicationFormNumber = ""; // generate application form number later in service
        String applicantName = jsonNode.get("applicantName").asText();
        String applicantId = jsonNode.get("applicantId").asText();
        String applicantCountry = jsonNode.get("applicantCountry").asText();
        String issueMerchantId = jsonNode.get("issueMerchantId").asText();
        BigDecimal totalAmount = jsonNode.get("totalAmount").decimalValue();
        // get invoices
        JsonNode invoicesNode = jsonNode.get("invoices");
        List<InvoiceModel> invoices = getInvoicesFromJson(invoicesNode);
        // application form model
        ApplicationFormModel applicationFormModel = new ApplicationFormModel();
        applicationFormModel.setApplicantName(applicantName);
        applicationFormModel.setApplicantId(applicantId);
        applicationFormModel.setApplicantCountry(applicantCountry);
        applicationFormModel.setTotalAmount(totalAmount);
        applicationFormModel.setInvoices(invoices);
        applicationFormModel.setIssueDate(CommonUtils.getCurrentDate());
        applicationFormModel.setIssueMerchantId(issueMerchantId);
        applicationFormService.createApplicationForm(applicationFormModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getApplicationForm", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getApplicationForm(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) throws BusinessException {
        ApplicationFormModel applicationFormModel = applicationFormService.getApplicationForm(applicationFormNumber);
        return CommonReturnType.create(applicationFormModel);
    }

    @RequestMapping(value = "/reviewApplicationForm", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_CUSTOMS})
    public CommonReturnType reviewApplicationForm(@RequestBody JsonNode jsonNode) throws BusinessException {
        String applicationFormNumber = jsonNode.get("applicationFormNumber").asText();
        List<ItemModel> items = new ArrayList<>();
        JsonNode itemsNode = jsonNode.get("rejectItems");
        items = getRejectedItemsFromJson(itemsNode);
        applicationFormService.reviewApplicationForm(applicationFormNumber, items);
        return CommonReturnType.create(null);
    }
}
