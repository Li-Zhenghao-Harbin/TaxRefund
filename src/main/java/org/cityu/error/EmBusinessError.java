package org.cityu.error;

public enum EmBusinessError implements CommonError {
    PARAMETER_VALIDATION_ERROR(10000, "Parameters illegal"),
    UNKNOWN_ERROR(10001, "Unknown error"),
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
