package com.kunteng.cyria.auth.service;

import com.kunteng.cyria.auth.domain.User;

public interface UserService {

	void create(User user);

	void updateUser(User us);

	boolean hasUser(String username);

	User findByUsername(String username);

	void saveUser(User user);

	User find(String subject);

}
