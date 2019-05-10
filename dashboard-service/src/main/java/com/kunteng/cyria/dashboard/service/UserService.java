package com.kunteng.cyria.dashboard.service;

import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.utils.CommonResult;
import com.kunteng.cyria.dashboard.domain.Translation;

import java.util.List;

public interface UserService {

	CommonResult getUserByUsername(String username);
	CommonResult createNewUser(User user);
	CommonResult userLogin(User user);
	CommonResult userLogout(String username);
}
