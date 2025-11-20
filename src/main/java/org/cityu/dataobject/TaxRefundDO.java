package org.cityu.dataobject;

import java.util.Date;

public class TaxRefundDO {
    private Integer id;

    private String applicationFormNumber;

    private Integer taxRefundMethod;

    private Date taxRefundDate;

    private String applicationFormMaterial;

    private String invoiceMaterial;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    public void setApplicationFormNumber(String applicationFormNumber) {
        this.applicationFormNumber = applicationFormNumber == null ? null : applicationFormNumber.trim();
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
}