package co.igorski.user;

import co.igorski.impl.UnsecureUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<UnsecureUser, Long> {
}
