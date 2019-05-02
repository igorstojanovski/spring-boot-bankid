package com.auth0.samples.authapi.springbootauthupdated.user;

import com.auth0.samples.authapi.springbootauthupdated.repositories.BankIdRepository;
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
            ApplicationUser applicationUser = new ApplicationUser();
            applicationUser.setId(bankId.getId());
            applicationUserRepository.save(applicationUser);
        }
    }
}
