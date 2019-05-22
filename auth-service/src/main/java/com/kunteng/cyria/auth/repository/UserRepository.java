package com.kunteng.cyria.auth.repository;

import com.kunteng.cyria.auth.domain.User;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
//public interface UserRepository extends CrudRepository<User, String> {
public interface UserRepository extends MongoRepository<User,String>{

	User findByUsername(String name);

	Optional<User> findById(String id);

}
