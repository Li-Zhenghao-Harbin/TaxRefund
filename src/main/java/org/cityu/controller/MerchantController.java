package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.MerchantOnly;
import org.cityu.common.utils.CommonUtils;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.ApplicationFormService;
import org.cityu.service.InvoiceService;
import org.cityu.service.MerchantService;
import org.cityu.service.UserService;
import org.cityu.service.model.ApplicationFormModel;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.controller.BaseController.CONTENT_TYPE_JSON;

@RestController
@RequestMapping("/merchant")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@MerchantOnly
public class MerchantController extends BaseController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private ApplicationFormService applicationFormService;

    @RequestMapping(value = "/createInvoice", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    public CommonReturnType createInvoice(@RequestBody JsonNode jsonNode) {
        String invoiceNumber = "";  // generate invoice number later in service
        String sellerTaxId = jsonNode.get("sellerTaxId").asText();
        BigDecimal amount = jsonNode.get("amount").decimalValue();

        List<ItemModel> items = getItemsFromJson(jsonNode.get("items"));
        InvoiceModel invoiceModel = new InvoiceModel();
        invoiceModel.setInvoiceNumber(invoiceNumber);
        invoiceModel.setSellerTaxId(sellerTaxId);
        invoiceModel.setAmount(amount);
        invoiceModel.setIssueDate(CommonUtils.getCurrentDate());
        invoiceModel.setStatus(1);
        invoiceModel.setItems(items);
        invoiceService.createInvoice(invoiceModel);
        return CommonReturnType.create(null);
    }

    private List<ItemModel> getItemsFromJson(JsonNode jsonNode) {
        List<ItemModel> items = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode itemNode : jsonNode) {
                ItemModel item = new ItemModel();
                item.setInvoiceNumber("");
                item.setItemName(itemNode.get("itemName").asText());
                item.setQuantity(itemNode.get("quantity").asInt());
                item.setUnitPrice(BigDecimal.valueOf(itemNode.get("unitPrice").asDouble()));
                item.setAmount(BigDecimal.valueOf(itemNode.get("amount").asDouble()));
                items.add(item);
            }
        }
        return items;
    }

    @RequestMapping(value = "/getInvoiceByInvoiceNumber", method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType getInvoiceByInvoiceNumber(@RequestParam(name = "invoiceNumber") String invoiceNumber) {
        InvoiceModel invoiceModel = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return CommonReturnType.create(invoiceModel);
    }

    @RequestMapping(value = "/createApplicationForm", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
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
        return CommonReturnType.create(applicationFormModel);
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
}
