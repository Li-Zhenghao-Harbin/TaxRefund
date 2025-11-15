package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.RequireRole;
import org.cityu.common.utils.CommonUtils;
import org.cityu.response.CommonReturnType;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.cityu.controller.BaseController.*;

@RestController
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/createInvoice", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    @RequireRole({ROLE_MERCHANT})
    public CommonReturnType createInvoice(@RequestBody JsonNode jsonNode) {
        String invoiceNumber = "";  // generate invoice number later in service
        String sellerTaxId = jsonNode.get("sellerTaxId").asText();
        BigDecimal totalAmount = jsonNode.get("totalAmount").decimalValue();

        List<ItemModel> items = getItemsFromJson(jsonNode.get("items"));
        InvoiceModel invoiceModel = new InvoiceModel();
        invoiceModel.setInvoiceNumber(invoiceNumber);
        invoiceModel.setSellerTaxId(sellerTaxId);
        invoiceModel.setTotalAmount(totalAmount);
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
                items.add(item);
            }
        }
        return items;
    }

    @RequestMapping(value = "/getInvoiceByInvoiceNumber", method = {RequestMethod.GET})
    @ResponseBody
    @RequireRole({ROLE_MERCHANT, ROLE_CUSTOMS})
    public CommonReturnType getInvoiceByInvoiceNumber(@RequestParam(name = "invoiceNumber") String invoiceNumber) {
        InvoiceModel invoiceModel = invoiceService.getInvoiceByInvoiceNumber(invoiceNumber);
        return CommonReturnType.create(invoiceModel);
    }
}
