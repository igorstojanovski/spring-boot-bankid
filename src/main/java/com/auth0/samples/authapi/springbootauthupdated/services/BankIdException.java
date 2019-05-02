package com.auth0.samples.authapi.springbootauthupdated.services;

public class BankIdException extends Exception {
    BankIdException(String message, Throwable e) {
        super(message, e);
    }

    public BankIdException(String message) {
        super(message);
    }
}
