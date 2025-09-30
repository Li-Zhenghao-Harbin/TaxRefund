package org.cityu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.common.annotation.MerchantOnly;
import org.cityu.common.utils.CommonUtils;
import org.cityu.dataobject.ItemDO;
import org.cityu.dataobject.UserDO;
import org.cityu.response.CommonReturnType;
import org.cityu.service.InvoiceService;
import org.cityu.service.MerchantService;
import org.cityu.service.UserService;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.cityu.controller.BaseController.CONTENT_TYPE_FORMED;
import static org.cityu.controller.BaseController.CONTENT_TYPE_JSON;

@RestController
@RequestMapping("/merchant")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
@MerchantOnly
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private UserService userService;

    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/createInvoice", method = {RequestMethod.POST}, consumes = CONTENT_TYPE_JSON)
    @ResponseBody
    public CommonReturnType createInvoice(@RequestBody JsonNode jsonNode) {
        String invoiceNumber = ""; //jsonNode.get("invoiceNumber").asText();
        String sellerTaxId = jsonNode.get("sellerTaxId").asText();
        BigDecimal amount = jsonNode.get("amount").decimalValue();

        JsonNode itemsNode = jsonNode.get("items");
        List<ItemModel> items = new ArrayList<>();

        if (itemsNode != null && itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                ItemModel item = new ItemModel();
                item.setInvoiceNumber(invoiceNumber);
                item.setItemName(itemNode.get("itemName").asText());
                item.setQuantity(itemNode.get("quantity").asInt());
                item.setUnitPrice(BigDecimal.valueOf(itemNode.get("unitPrice").asDouble()));
                item.setAmount(BigDecimal.valueOf(itemNode.get("amount").asDouble()));
                items.add(item);
            }
        }
        InvoiceModel invoiceModel = new InvoiceModel();
        invoiceModel.setInvoiceNumber(invoiceNumber);
        invoiceModel.setSellerTaxId(sellerTaxId);
        invoiceModel.setAmount(amount);
        invoiceModel.setIssueDate(CommonUtils.getCurrentDate());
        invoiceModel.setStatus(1);
        invoiceModel.setItems(items);
        invoiceService.createInvoice(invoiceModel);
        System.out.println(items.size());
        return CommonReturnType.create(null);
    }
}
