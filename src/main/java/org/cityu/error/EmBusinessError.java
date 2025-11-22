package org.cityu.error;

public enum EmBusinessError implements CommonError {
    PARAMETER_VALIDATION_ERROR(10000, "Parameters illegal"),
    UNKNOWN_ERROR(10001, "Unknown error"),
    // user
    USER_NOT_EXIST(20000, "Failed to login"),
    PASSWORD_NOT_MATCH(20001, "Password not match"),
    // invoice
    INVOICE_NOT_EXIST(30000, "Invoice not exist"),
    IMPROPER_INVOICE_STATUS(30001, "Improper invoice status"),
    // application form
    APPLICATION_FORM_NOT_EXIST(40000, "Application form not exist"),
    IMPROPER_APPLICATION_STATUS(40001, "Improper application status"),
    APPLICATION_FORM_NOT_REVIEWED(40002, "Application form not reviewed"),
    // tax refund,
    REPEAT_TAX_REFOUND(50000, "The application form has already been tax refunded")
    ;

    private EmBusinessError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    private int errorCode;
    private String errorMessage;

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public CommonError setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
