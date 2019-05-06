package com.auth0.samples.authapi.springbootauthupdated.repositories;

import com.auth0.samples.authapi.springbootauthupdated.model.BankId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankIdRepository extends CrudRepository<BankId, Long> {
    BankId findBankIdByBankId(String bankId);
}
