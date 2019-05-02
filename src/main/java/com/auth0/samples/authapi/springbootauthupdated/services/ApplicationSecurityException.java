package com.auth0.samples.authapi.springbootauthupdated.services;

class ApplicationSecurityException extends RuntimeException {
    ApplicationSecurityException(String message, Exception e) {
        super(message, e);
    }
}
