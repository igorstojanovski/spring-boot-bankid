package co.igorski.user;

import co.igorski.impl.UnsecureUser;
import co.igorski.model.BankId;
import co.igorski.services.bankId.BankIdService;
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
        BankId bankId = bankIdService.find(username);
        UnsecureUser unsecureUser;

        if(bankId == null) {
            bankId = new BankId();
            bankId.setBankId(username);
            bankId = bankIdService.createBankId(bankId);

            unsecureUser = new UnsecureUser();
            unsecureUser.setId(bankId.getId());

            unsecureUser = applicationUserRepository.save(unsecureUser);
        } else {
            unsecureUser = applicationUserRepository.findById(bankId.getId()).get();
        }

        return new User(String.valueOf(unsecureUser.getId()), bankId.getBankId(), emptyList());
    }
}
