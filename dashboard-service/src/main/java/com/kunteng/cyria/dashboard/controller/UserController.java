package com.kunteng.cyria.dashboard.controller;

import com.kunteng.cyria.dashboard.domain.User;
import com.kunteng.cyria.dashboard.service.UserService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(path = "/user/{id}/info", method = RequestMethod.GET)
	public CommonResult getUserByUsername(@PathVariable String id ){
		return userService.getUserByUsername(id);
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public CommonResult createNewUser(@Valid @RequestBody User user) {
		return userService.createNewUser(user);
	}

	@RequestMapping(path = "/user/login", method = RequestMethod.POST)
	public CommonResult userLogin(@RequestBody User user) {
		return userService.userLogin(user);
	}
	
	@RequestMapping(path = "/user/logout", method = RequestMethod.POST)
	public CommonResult userLogout(@RequestBody String username){
		return userService.userLogout(username);
	}
}
