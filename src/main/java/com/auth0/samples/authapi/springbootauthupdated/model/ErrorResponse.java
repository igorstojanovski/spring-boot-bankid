package com.auth0.samples.authapi.springbootauthupdated.model;

public class ErrorResponse {
    private String errorCode;
    private String details;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
