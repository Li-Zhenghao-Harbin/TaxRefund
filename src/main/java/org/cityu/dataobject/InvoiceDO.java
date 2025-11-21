package org.cityu.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceDO {
    private String invoiceNumber;

    private String sellerTaxId;

    private BigDecimal totalAmount;

    private Date issueDate;

    private String applicationFormNumber;

    private Integer status;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber == null ? null : invoiceNumber.trim();
    }

    public String getSellerTaxId() {
        return sellerTaxId;
    }

    public void setSellerTaxId(String sellerTaxId) {
        this.sellerTaxId = sellerTaxId == null ? null : sellerTaxId.trim();
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    public void setApplicationFormNumber(String applicationFormNumber) {
        this.applicationFormNumber = applicationFormNumber == null ? null : applicationFormNumber.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}