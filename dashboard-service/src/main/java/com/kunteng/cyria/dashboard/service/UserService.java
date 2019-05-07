package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.domain.Translation;

import java.util.List;

public interface UserService {

    User getUserByUsername(String username);
	User createNewUser(User user);
	String userLogin(User user);
	void userLogout(String username);
}
