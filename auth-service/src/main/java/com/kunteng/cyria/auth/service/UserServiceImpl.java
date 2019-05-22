package com.kunteng.cyria.auth.service;

import com.kunteng.cyria.auth.domain.User;
import com.kunteng.cyria.auth.repository.UserRepository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private UserRepository repository;

	@Override
	public void create(User user) {

		User existing = repository.findOne(user.getUsername());
		Assert.isNull(existing, "user already exists: " + user.getUsername());

		String hash = encoder.encode(user.getPassword());
		user.setPassword(hash);

		repository.save(user);

		log.info("new user has been created: {}", user.getUsername());
	}
	
	public boolean hasUser(String name) {
		User dbuser = repository.findByUsername(name);
		if(dbuser == null) {
			return false;
		}else {
			log.info("this user in db: "+ dbuser.toString());
			return true;
		}
	}
	
	public void saveUser(User u) {
		repository.save(u);
	}
	
	public User find(String id) {
		Optional<User> opt = repository.findById(id);
		return opt.orElse(null);
	}
	
	public User findByUsername(String username) {
		User dbuser = repository.findByUsername(username);
		return dbuser;
	}
	
	public void updateUser(User u) {
		repository.save(u);
	}
}
