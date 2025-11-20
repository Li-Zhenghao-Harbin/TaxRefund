package org.cityu.dataobject;

public class BankCardDO {
    private String applicationFormNumber;

    private String bankCardNumber;

    private String bankCardHolder;

    private String bankName;

    public String getApplicationFormNumber() {
        return applicationFormNumber;
    }

    public void setApplicationFormNumber(String applicationFormNumber) {
        this.applicationFormNumber = applicationFormNumber == null ? null : applicationFormNumber.trim();
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber == null ? null : bankCardNumber.trim();
    }

    public String getBankCardHolder() {
        return bankCardHolder;
    }

    public void setBankCardHolder(String bankCardHolder) {
        this.bankCardHolder = bankCardHolder == null ? null : bankCardHolder.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }
}