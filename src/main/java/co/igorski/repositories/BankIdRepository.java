package co.igorski.repositories;

import co.igorski.model.BankId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankIdRepository extends CrudRepository<BankId, Long> {
    BankId findBankIdByBankId(String bankId);
}
