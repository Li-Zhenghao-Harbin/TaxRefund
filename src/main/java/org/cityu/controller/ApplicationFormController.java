package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

import static org.cityu.common.utils.JsonUtils.*;

@RestController
@RequestMapping("/applicationForm")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class ApplicationFormController extends BaseController {
    @Autowired
    private ApplicationFormService applicationFormService;

    @RequestMapping(value = "/createApplicationForm", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_MERCHANT})
    public CommonReturnType createApplicationForm(@RequestBody JsonNode jsonNode) throws ParseException, BusinessException {
        String applicantName = jsonNode.get("applicantName").asText();
        String applicantId = jsonNode.get("applicantId").asText();
        String applicantCountry = jsonNode.get("applicantCountry").asText();
        // get invoices
        JsonNode invoicesNode = jsonNode.get("invoices");
        List<InvoiceModel> invoices = getInvoicesFromJson(invoicesNode);
        // application form model
        ApplicationFormModel applicationFormModel = new ApplicationFormModel();
        applicationFormModel.setApplicantName(applicantName);
        applicationFormModel.setApplicantId(applicantId);
        applicationFormModel.setApplicantCountry(applicantCountry);
        applicationFormModel.setInvoices(invoices);
        applicationFormModel.setIssueDate(CommonUtils.getCurrentDate());
        applicationFormService.createApplicationForm(applicationFormModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getApplicationFormByApplicationNumber", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getApplicationFormByApplicationNumber(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) throws BusinessException {
        ApplicationFormModel applicationFormModel = applicationFormService.getApplicationFormByApplicationNumber(applicationFormNumber);
        return CommonReturnType.create(applicationFormModel);
    }

    @RequestMapping(value = "/getApplicationFormsByIssueMerchantName", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getApplicationFormsByIssueMerchantName(@RequestParam(name = "issueMerchantName") String issueMerchantName) throws BusinessException {
        List<ApplicationFormModel> applicationFormModel = applicationFormService.getApplicationFormsByIssueMerchantName(issueMerchantName);
        return CommonReturnType.create(applicationFormModel);
    }

    @RequestMapping(value = "/getAllApplicationForms", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getAllApplicationForms() throws BusinessException {
        List<ApplicationFormModel> applicationFormModel = applicationFormService.getAllApplicationForms();
        return CommonReturnType.create(applicationFormModel);
    }

    @RequestMapping(value = "/reviewApplicationForm", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_CUSTOMS})
    public CommonReturnType reviewApplicationForm(@RequestBody JsonNode jsonNode) throws BusinessException {
        String applicationFormNumber = jsonNode.get("applicationFormNumber").asText();
        JsonNode itemsNode = jsonNode.get("rejectItems");
        List<ItemModel> items = getRejectedItemsFromJson(itemsNode);
        applicationFormService.reviewApplicationForm(applicationFormNumber, items);
        return CommonReturnType.create(null);
    }
}
