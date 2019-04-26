package com.auth0.samples.authapi.springbootauthupdated.services;

import com.auth0.samples.authapi.springbootauthupdated.repositories.BankIdRepository;
import com.auth0.samples.authapi.springbootauthupdated.user.BankId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankIdService {

    @Autowired
    private BankIdRepository bankIdRepository;

    public BankId getBankId(String bankId) {
        return bankIdRepository.findBankIdByBankId(bankId);
    }

    public BankId createBankId(BankId bankId) {
        return bankIdRepository.save(bankId);
    }
}
