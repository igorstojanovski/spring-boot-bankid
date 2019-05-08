package co.igorski.impl;

import co.igorski.model.AuthResponse;
import co.igorski.model.BankIdUser;
import co.igorski.model.CollectResponse;
import co.igorski.model.CompletitionData;
import co.igorski.services.bankId.BankIdAuthenticator;
import co.igorski.services.bankId.BankIdException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Profile("default")
public class DummyBankIdAuthenticator implements BankIdAuthenticator {

    private final Map<String, String> personalNumbers = new HashMap<>();

    @Override
    public AuthResponse auth(String personalNumber, String ipAddress) throws BankIdException {
        AuthResponse authResponse = new AuthResponse();
        if (personalNumbers.containsKey(personalNumber)) {
            authResponse.setOrderRef(personalNumbers.get(personalNumber));
        } else {
            String refId = RandomStringUtils.randomAlphabetic(10);
            authResponse.setOrderRef(refId);
            personalNumbers.put(personalNumber, refId);
        }

        return authResponse;
    }

    @Override
    public CollectResponse collect(String refId) throws BankIdException {
        if(personalNumbers.values().contains(refId)) {
            String personalNumber = "";
            for(Map.Entry<String, String> entry : personalNumbers.entrySet()) {
                if(entry.getValue().equals(refId)) {
                    personalNumber = entry.getKey();
                    break;
                }
            }
            personalNumbers.remove(personalNumber);
            CollectResponse collectResponse = new CollectResponse();
            collectResponse.setStatus("complete");

            CompletitionData completitionData = new CompletitionData();
            BankIdUser bankIdUser = new BankIdUser();
            bankIdUser.setPersonalNumber(personalNumber);
            completitionData.setUser(bankIdUser);
            collectResponse.setCompletitionData(completitionData);
            return collectResponse;
        }
        return null;
    }
}
