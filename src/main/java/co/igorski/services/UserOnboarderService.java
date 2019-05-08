package co.igorski.services;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserOnboarderService {
    UserDetails onBoard(Long id);
}
