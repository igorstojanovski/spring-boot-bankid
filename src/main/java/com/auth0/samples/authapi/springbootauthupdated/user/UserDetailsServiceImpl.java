package com.auth0.samples.authapi.springbootauthupdated.user;

import com.auth0.samples.authapi.springbootauthupdated.services.BankIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BankIdService bankIdService;
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository, BankIdService bankIdService) {
        this.applicationUserRepository = applicationUserRepository;
        this.bankIdService = bankIdService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BankId bankId = bankIdService.getBankId(username);
        ApplicationUser applicationUser;

        if(bankId == null) {
            bankId = new BankId();
            bankId.setBankId(username);
            bankId = bankIdService.createBankId(bankId);

            applicationUser = new ApplicationUser();
            applicationUser.setId(bankId.getId());
            applicationUser.setPassword(bankId.getBankId());
            applicationUser.setUsername(bankId.getBankId());

            applicationUser = applicationUserRepository.save(applicationUser);
        } else {
            applicationUser = applicationUserRepository.findById(bankId.getId()).get();
        }

        return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
    }
}
