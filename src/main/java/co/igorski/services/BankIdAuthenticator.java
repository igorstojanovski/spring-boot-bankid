package co.igorski.services;

import co.igorski.model.AuthResponse;
import co.igorski.model.CollectResponse;

public interface BankIdAuthenticator {
    AuthResponse auth(String personalNumber, String ipAddress) throws BankIdException;
    CollectResponse collect(String refId) throws BankIdException;
}
