package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

@Service
public class UserServiceImpl implements UserService {

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository userRepository;

	public User getUserByUsername(String username){
		return userRepository.findUserByUsername(username);
	}

	public User createNewUser(User user) {
		return userRepository.save(user);
	}

	public String userLogin(User user) {
		User usr = userRepository.findUserByUsername(user.getUsername());
		if(usr.getPassword().equals(user.getPassword())){
			return usr.getUsername();
		}else{
			return "failed";
		}
	}

	public void userLogout(String username){
		User user = userRepository.findUserByUsername(username);
	}
}
