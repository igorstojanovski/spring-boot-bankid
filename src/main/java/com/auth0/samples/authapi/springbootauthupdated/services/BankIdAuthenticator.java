package com.auth0.samples.authapi.springbootauthupdated.services;

import com.auth0.samples.authapi.springbootauthupdated.model.AuthResponse;
import com.auth0.samples.authapi.springbootauthupdated.model.CollectResponse;

public interface BankIdAuthenticator {
    AuthResponse auth(String personalNumber, String ipAddress) throws BankIdException;
    CollectResponse collect(String refId) throws BankIdException;
}
