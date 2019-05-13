package com.kunteng.cyria.dashboard.controller;

import com.kunteng.cyria.dashboard.domain.Account;
import com.kunteng.cyria.dashboard.service.AccountService;
import com.kunteng.cyria.dashboard.utils.CommonResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(path = "/user/{id}/info", method = RequestMethod.GET)
	public CommonResult getUserByUsername(@PathVariable String id ){
		return accountService.getAccountByUsername(id);
	}

	@RequestMapping(path = "/user", method = RequestMethod.POST)
	public CommonResult createNewUser(@Valid @RequestBody Account account) {
		return accountService.createNewAccount(account);
	}

	@RequestMapping(path = "/user/login", method = RequestMethod.POST)
	public CommonResult accountLogin(@RequestBody Account account) {
		return accountService.accountLogin(account);
	}
	
	@RequestMapping(path = "/user/logout", method = RequestMethod.POST)
	public CommonResult accountLogout(@RequestBody String username){
		return accountService.accountLogout(username);
	}
}
