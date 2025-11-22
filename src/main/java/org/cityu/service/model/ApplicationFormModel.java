package org.cityu.service.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ApplicationFormModel {
    private String applicationFormNumber;
    private String applicantName;
    private String applicantId;
    private String applicantCountry;
    private Date issueDate;
    private String issueMerchantName;
    private BigDecimal totalAmount;
    private BigDecimal customsConfirmAmount;
    private Integer status;
    private List<InvoiceModel> invoices;

    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    public void setApplicationFormNumber(String applicationFormNumber) {
        this.applicationFormNumber = applicationFormNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantCountry() {
        return applicantCountry;
    }

    public void setApplicantCountry(String applicantCountry) {
        this.applicantCountry = applicantCountry;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
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

    public BigDecimal getCustomsConfirmAmount() {
        return customsConfirmAmount;
    }

    public void setCustomsConfirmAmount(BigDecimal customsConfirmAmount) {
        this.customsConfirmAmount = customsConfirmAmount;
    }

    public List<InvoiceModel> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceModel> invoices) {
        this.invoices = invoices;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
