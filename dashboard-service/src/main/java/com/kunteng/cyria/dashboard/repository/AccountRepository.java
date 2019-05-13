package com.kunteng.cyria.dashboard.repository;

import com.kunteng.cyria.dashboard.domain.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
	Account findAccountByUsername(String username);
}
