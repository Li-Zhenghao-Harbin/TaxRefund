package org.cityu.controller;

import org.cityu.response.CommonReturnType;
import org.cityu.service.InvoiceService;
import org.cityu.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

import static org.cityu.controller.BaseController.CONTENT_TYPE_FORMED;

@RestController
@RequestMapping("/invoice")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*", originPatterns = "*")
public class InvoiceController {
    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/createInvoice", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType createInvoice(@RequestParam(name = "invoiceNumber") String invoiceNumber,
                                          @RequestParam(name = "sellerTaxId") String sellerTaxId,
                                          @RequestParam(name = "amount") BigDecimal amount,
                                          @RequestParam(name = "issueDate") Date issueDate) {
        return CommonReturnType.create(null);
    }
}
