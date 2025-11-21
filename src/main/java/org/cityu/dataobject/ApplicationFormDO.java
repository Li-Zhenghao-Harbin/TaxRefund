package org.cityu.dataobject;

import java.math.BigDecimal;
import java.util.Date;

public class ApplicationFormDO {
    private String applicationFormNumber;

    private String applicantName;

    private String applicantId;

    private String applicantCountry;

    private Date issueDate;

    private Integer issueMerchantId;

    private BigDecimal totalAmount;

    private BigDecimal customsConfirmAmount;

    private Integer status;

    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    public void setApplicationFormNumber(String applicationFormNumber) {
        this.applicationFormNumber = applicationFormNumber == null ? null : applicationFormNumber.trim();
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName == null ? null : applicantName.trim();
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId == null ? null : applicantId.trim();
    }

    public String getApplicantCountry() {
        return applicantCountry;
    }

    public void setApplicantCountry(String applicantCountry) {
        this.applicantCountry = applicantCountry == null ? null : applicantCountry.trim();
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getIssueMerchantId() {
        return issueMerchantId;
    }

    public void setIssueMerchantId(Integer issueMerchantId) {
        this.issueMerchantId = issueMerchantId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}