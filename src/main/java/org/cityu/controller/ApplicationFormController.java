package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.controller.BaseController.*;

@RestController
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
        applicationFormModel.setCustomsConfirmAmount(new BigDecimal(0));
        applicationFormModel.setInvoices(invoices);
        applicationFormModel.setIssueDate(CommonUtils.getCurrentDate());
        applicationFormModel.setIssueMerchantId(issueMerchantId);
        applicationFormService.createApplicationForm(applicationFormModel);
        return CommonReturnType.create(null);
    }

    private List<InvoiceModel> getInvoicesFromJson(JsonNode jsonNode) throws ParseException {
        List<InvoiceModel> invoices = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode invoiceNode : jsonNode) {
                InvoiceModel invoice = new InvoiceModel();
                invoice.setInvoiceNumber(invoiceNode.get("invoiceNumber").asText());
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    @RequestMapping(value = "/getApplicationForm", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getApplicationForm(@RequestParam(name = "applicationFormNumber") String applicationFormNumber) {
        ApplicationFormModel applicationFormModel = applicationFormService.getApplicationForm(applicationFormNumber);
        return CommonReturnType.create(applicationFormModel);
    }
}
