package com.kunteng.cyria.dashboard.repository;

import com.kunteng.cyria.dashboard.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
	User findUserByUsername(String username);
}
