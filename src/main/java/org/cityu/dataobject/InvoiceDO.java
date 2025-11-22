package org.cityu.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceDO {
    private String invoiceNumber;

    private String issueMerchantName;

    private BigDecimal totalAmount;

    private Date issueDate;

    private String applicationFormNumber;

    private Integer status;

    private String company;

    private String sellerTaxId;

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber == null ? null : invoiceNumber.trim();
    }

    public String getIssueMerchantName() {
        return issueMerchantName;
    }

    public void setIssueMerchantName(String issueMerchantName) {
        this.issueMerchantName = issueMerchantName;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSellerTaxId() {
        return sellerTaxId;
    }

    public void setSellerTaxId(String sellerTaxId) {
        this.sellerTaxId = sellerTaxId;
    }
}