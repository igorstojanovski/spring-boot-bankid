package com.auth0.samples.authapi.springbootauthupdated.services;

import com.auth0.samples.authapi.springbootauthupdated.model.AuthResponse;
import com.auth0.samples.authapi.springbootauthupdated.model.BankId;
import com.auth0.samples.authapi.springbootauthupdated.model.CollectResponse;
import com.auth0.samples.authapi.springbootauthupdated.repositories.BankIdRepository;
import com.auth0.samples.authapi.springbootauthupdated.user.ApplicationUser;
import com.auth0.samples.authapi.springbootauthupdated.user.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankIdService {
    private final ApplicationUserRepository applicationUserRepository;
    private BankIdRepository bankIdRepository;
    private BankIdAuthenticator bankIdAuthenticator;

    @Autowired
    public BankIdService(BankIdRepository bankIdRepository, BankIdAuthenticator bankIdAuthenticator,
                         ApplicationUserRepository applicationUserRepository) {
        this.bankIdRepository = bankIdRepository;
        this.bankIdAuthenticator =bankIdAuthenticator;
        this.applicationUserRepository = applicationUserRepository;
    }

    public BankId find(String bankId) {
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

    public BankId onboard(BankId bankId) {
        BankId internal = bankIdRepository.findBankIdByBankId(bankId.getBankId());
        if(internal == null) {
            internal = bankIdRepository.save(bankId);
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setId(internal.getId());
            applicationUserRepository.save(applicationUser);
        }
        return internal;
    }

    public boolean isValid(String bankId) {
        return bankIdRepository.findBankIdByBankId(bankId) != null;
    }
}
