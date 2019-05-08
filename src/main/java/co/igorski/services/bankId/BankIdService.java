package co.igorski.services.bankId;

import co.igorski.model.AuthResponse;
import co.igorski.model.BankId;
import co.igorski.model.CollectResponse;
import co.igorski.repositories.BankIdRepository;
import co.igorski.services.UserOnboarderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankIdService {
    private final UserOnboarderService userOnboarderService;
    private BankIdRepository bankIdRepository;
    private BankIdAuthenticator bankIdAuthenticator;

    @Autowired
    public BankIdService(BankIdRepository bankIdRepository, BankIdAuthenticator bankIdAuthenticator,
                         UserOnboarderService userOnboarderService) {
        this.bankIdRepository = bankIdRepository;
        this.bankIdAuthenticator =bankIdAuthenticator;
        this.userOnboarderService = userOnboarderService;
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
            userOnboarderService.onBoard(internal.getId());
        }
        return internal;
    }

    public boolean isValid(String bankId) {
        return bankIdRepository.findBankIdByBankId(bankId) != null;
    }
}
