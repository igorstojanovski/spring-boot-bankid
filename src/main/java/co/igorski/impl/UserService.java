package co.igorski.impl;

import co.igorski.services.UserOnboarderService;
import co.igorski.repositories.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserOnboarderService {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails onBoard(Long id) {
        UnsecureUser unsecureUser = new UnsecureUser();
        unsecureUser.setId(id);
        applicationUserRepository.save(unsecureUser);
        return null;
    }
}
