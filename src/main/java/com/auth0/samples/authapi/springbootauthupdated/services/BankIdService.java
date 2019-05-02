package com.auth0.samples.authapi.springbootauthupdated.services;

import com.auth0.samples.authapi.springbootauthupdated.model.AuthResponse;
import com.auth0.samples.authapi.springbootauthupdated.model.CollectResponse;
import com.auth0.samples.authapi.springbootauthupdated.repositories.BankIdRepository;
import com.auth0.samples.authapi.springbootauthupdated.user.BankId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankIdService {
    private BankIdRepository bankIdRepository;
    private BankIdAuthenticator bankIdAuthenticator;

    @Autowired
    public BankIdService(BankIdRepository bankIdRepository, BankIdAuthenticator bankIdAuthenticator) {
        this.bankIdRepository = bankIdRepository;
        this.bankIdAuthenticator =bankIdAuthenticator;
    }

    public BankId getBankId(String bankId) {
        return bankIdRepository.findBankIdByBankId(bankId);
    }

    public BankId createBankId(BankId bankId) {
        return bankIdRepository.save(bankId);
    }

    public AuthResponse auth(String personalNumber, String ipAddress) throws BankIdException {
        return bankIdAuthenticator.auth(personalNumber, ipAddress);
    }

    public CollectResponse collect(String refId) throws BankIdException {
        return bankIdAuthenticator.collect(refId);
    }
}
