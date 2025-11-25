package org.cityu.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import org.cityu.service.model.InvoiceModel;
import org.cityu.service.model.ItemModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List<ItemModel> getItemsFromJson(JsonNode jsonNode) {
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

    public static List<InvoiceModel> getInvoicesFromJson(JsonNode jsonNode) {
        List<InvoiceModel> invoices = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode invoiceNode : jsonNode) {
                InvoiceModel invoice = new InvoiceModel();
                invoice.setInvoiceNumber(invoiceNode.asText());
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public static List<ItemModel> getRejectedItemsFromJson(JsonNode jsonNode) {
        List<ItemModel> items = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode itemNode : jsonNode) {
                ItemModel item = new ItemModel();
                item.setId(itemNode.asInt());
                items.add(item);
            }
        }
        return items;
    }

    public static List<String> getApplicationFormNumbersFromJson(JsonNode jsonNode) {
        List<String> applicationFormNumbers = new ArrayList<>();
        if (jsonNode != null && jsonNode.isArray()) {
            for (JsonNode applicationFormNumberNode : jsonNode) {
                applicationFormNumbers.add(applicationFormNumberNode.asText());
            }
        }
        return applicationFormNumbers;
    }
}
