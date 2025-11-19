package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.error.BusinessException;
import org.cityu.response.CommonReturnType;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.common.utils.JsonUtils.getItemsFromJson;
import static org.cityu.controller.BaseController.*;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/createInvoice", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_MERCHANT})
    public CommonReturnType createInvoice(@RequestBody JsonNode jsonNode) {
        String invoiceNumber = "";  // generate invoice number later in service
//        String sellerTaxId = jsonNode.get("sellerTaxId").asText();
        List<ItemModel> items = getItemsFromJson(jsonNode.get("items"));
        InvoiceModel invoiceModel = new InvoiceModel();
        invoiceModel.setInvoiceNumber(invoiceNumber);
//        invoiceModel.setSellerTaxId(sellerTaxId);
        invoiceModel.setIssueDate(CommonUtils.getCurrentDate());
        invoiceModel.setStatus(1);
        invoiceModel.setItems(items);
        invoiceService.createInvoice(invoiceModel);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getInvoiceByInvoiceNumber", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getInvoiceByInvoiceNumber(@RequestParam(name = "invoiceNumber") String invoiceNumber) throws BusinessException {
        InvoiceModel invoiceModel = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return CommonReturnType.create(invoiceModel);
    }
}
