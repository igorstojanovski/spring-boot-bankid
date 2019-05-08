package co.igorski.user;

import co.igorski.impl.UnsecureUser;
import co.igorski.model.BankId;
import co.igorski.repositories.BankIdRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final BankIdRepository bankIdRepository;
    private ApplicationUserRepository applicationUserRepository;

    public UserController(ApplicationUserRepository applicationUserRepository,
                          BankIdRepository bankIdRepository) {
        this.applicationUserRepository = applicationUserRepository;
        this.bankIdRepository = bankIdRepository;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody BankId bankId) {

        if(bankIdRepository.findBankIdByBankId(bankId.getBankId()) == null) {
            bankId = bankIdRepository.save(bankId);
            UnsecureUser unsecureUser = new UnsecureUser();
            unsecureUser.setId(bankId.getId());
            applicationUserRepository.save(unsecureUser);
        }
    }
}
