package org.cityu.service.model;

import java.util.Date;
import java.util.List;

public class TaxRefundModel {
    private List<String> applicationFormNumbers;
    private Integer taxRefundMethod;
    private Date taxRefundDate;
    private String applicationFormMaterial;
    private String invoiceMaterial;
    private String bankCardNumber;
    private String bankCardHolder;
    private String bankName;

    public List<String> getApplicationFormNumbers() {
        return applicationFormNumbers;
    }

    public void setApplicationFormNumbers(List<String> applicationFormNumbers) {
        this.applicationFormNumbers = applicationFormNumbers;
    }

    public Integer getTaxRefundMethod() {
        return taxRefundMethod;
    }

    public void setTaxRefundMethod(Integer taxRefundMethod) {
        this.taxRefundMethod = taxRefundMethod;
    }

    public Date getTaxRefundDate() {
        return taxRefundDate;
    }

    public void setTaxRefundDate(Date taxRefundDate) {
        this.taxRefundDate = taxRefundDate;
    }

    public String getApplicationFormMaterial() {
        return applicationFormMaterial;
    }

    public void setApplicationFormMaterial(String applicationFormMaterial) {
        this.applicationFormMaterial = applicationFormMaterial;
    }

    public String getInvoiceMaterial() {
        return invoiceMaterial;
    }

    public void setInvoiceMaterial(String invoiceMaterial) {
        this.invoiceMaterial = invoiceMaterial;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardHolder() {
        return bankCardHolder;
    }

    public void setBankCardHolder(String bankCardHolder) {
        this.bankCardHolder = bankCardHolder;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
